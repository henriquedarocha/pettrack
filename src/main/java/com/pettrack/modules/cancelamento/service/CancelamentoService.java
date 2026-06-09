package com.pettrack.modules.cancelamento.service;

import com.pettrack.modules.cancelamento.domain.entity.SolicitacaoCancelamento;
import com.pettrack.modules.cancelamento.domain.enums.StatusCancelamento;
import com.pettrack.modules.cancelamento.dto.request.CancelamentoRequest;
import com.pettrack.modules.cancelamento.dto.response.CancelamentoResponse;
import com.pettrack.modules.cancelamento.repository.CancelamentoRepository;
import com.pettrack.modules.ecommerce.domain.entity.ItemPedido;
import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.repository.PedidoRepository;
import com.pettrack.modules.estoque.domain.enums.StatusItemEstoque;
import com.pettrack.modules.estoque.repository.ItemEstoqueRepository;
import com.pettrack.modules.rastreamento.service.RastreamentoService;
import com.pettrack.shared.exception.NegocioException;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelamentoService {

    private final CancelamentoRepository cancelamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final ItemEstoqueRepository itemEstoqueRepository;
    private final RastreamentoService rastreamentoService;

    @Transactional
    public CancelamentoResponse solicitarCancelamento(CancelamentoRequest request) {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));

        if (cancelamentoRepository.existsByPedidoId(pedido.getId())) {
            throw new NegocioException("Já existe uma solicitação de cancelamento para este pedido");
        }

        StatusPedido statusAtual = pedido.getStatus();

        // Cancelamento bloqueado — van já saiu ou entregue
        if (statusAtual == StatusPedido.EM_ROTA_ENTREGA ||
                statusAtual == StatusPedido.ENTREGUE ||
                statusAtual == StatusPedido.CANCELAMENTO_BLOQUEADO) {

            SolicitacaoCancelamento solicitacao = SolicitacaoCancelamento.builder()
                    .pedido(pedido)
                    .motivo(request.getMotivo())
                    .descricao(request.getDescricao())
                    .statusPedidoMomento(statusAtual)
                    .status(StatusCancelamento.BLOQUEADO)
                    .bloqueadoMotivo("Cancelamento bloqueado — pedido já está em rota de entrega ou entregue")
                    .dataResolucao(LocalDateTime.now())
                    .build();

            log.warn("Cancelamento bloqueado — Pedido: {}, Status: {}",
                    pedido.getNumeroPedido(), statusAtual);

            return toResponse(cancelamentoRepository.save(solicitacao));
        }

        // Antes do transporte — devolve ao estoque diretamente
        boolean antesDoTransporte = statusAtual == StatusPedido.PEDIDO_CRIADO
                || statusAtual == StatusPedido.AGUARDANDO_SEPARACAO
                || statusAtual == StatusPedido.EM_SEPARACAO
                || statusAtual == StatusPedido.AGUARDANDO_EMBALAGEM
                || statusAtual == StatusPedido.EMBALADO
                || statusAtual == StatusPedido.NA_GAIOLA
                || statusAtual == StatusPedido.NO_PALLET;

        // Após o transporte — precisa retornar ao CD primeiro
        boolean aposTransporte = statusAtual == StatusPedido.AGUARDANDO_TRANSPORTE
                || statusAtual == StatusPedido.EM_TRANSITO_CD_FILIAL
                || statusAtual == StatusPedido.RECEBIDO_NA_FILIAL
                || statusAtual == StatusPedido.EM_SEPARACAO_FILIAL;

        boolean requerRetornoCD = aposTransporte;

        // Devolve itens ao estoque se ainda não saíram do CD
        if (antesDoTransporte) {
            for (ItemPedido item : pedido.getItens()) {
                if (item.getItemEstoque() != null) {
                    item.getItemEstoque().setStatus(StatusItemEstoque.DISPONIVEL);
                    itemEstoqueRepository.save(item.getItemEstoque());
                }
            }
            pedido.setStatus(StatusPedido.CANCELADO_ESTOQUE);
        } else {
            pedido.setStatus(StatusPedido.CANCELADO_RETORNO_CD);
        }

        pedidoRepository.save(pedido);

        SolicitacaoCancelamento solicitacao = SolicitacaoCancelamento.builder()
                .pedido(pedido)
                .motivo(request.getMotivo())
                .descricao(request.getDescricao())
                .statusPedidoMomento(statusAtual)
                .status(StatusCancelamento.APROVADO)
                .requerRetornoCD(requerRetornoCD)
                .dataResolucao(LocalDateTime.now())
                .build();

        rastreamentoService.registrar(pedido,
                statusAtual,
                pedido.getStatus(),
                null,
                requerRetornoCD ? "Em trânsito — retorno ao CD necessário" : "CD",
                "Cancelamento aprovado — Motivo: " + request.getMotivo());

        log.info("Cancelamento aprovado — Pedido: {}, Requer retorno CD: {}",
                pedido.getNumeroPedido(), requerRetornoCD);

        return toResponse(cancelamentoRepository.save(solicitacao));
    }

    @Transactional
    public CancelamentoResponse confirmarRetornoCD(UUID cancelamentoId) {
        SolicitacaoCancelamento solicitacao = buscarSolicitacao(cancelamentoId);

        if (!solicitacao.isRequerRetornoCD()) {
            throw new NegocioException("Este cancelamento não requer retorno ao CD");
        }

        if (solicitacao.getStatus() != StatusCancelamento.APROVADO) {
            throw new NegocioException("Solicitação não está aprovada");
        }

        Pedido pedido = solicitacao.getPedido();

        for (ItemPedido item : pedido.getItens()) {
            if (item.getItemEstoque() != null) {
                item.getItemEstoque().setStatus(StatusItemEstoque.DEVOLVIDO);
                itemEstoqueRepository.save(item.getItemEstoque());
            }
        }

        pedido.setStatus(StatusPedido.DEVOLVIDO_AO_ESTOQUE);
        pedidoRepository.save(pedido);

        solicitacao.setStatus(StatusCancelamento.CONCLUIDO);
        solicitacao.setDataResolucao(LocalDateTime.now());

        rastreamentoService.registrar(pedido,
                StatusPedido.CANCELADO_RETORNO_CD,
                StatusPedido.DEVOLVIDO_AO_ESTOQUE,
                null,
                "CD",
                "Produto retornou ao CD e foi devolvido ao estoque");

        return toResponse(cancelamentoRepository.save(solicitacao));
    }

    @Transactional(readOnly = true)
    public CancelamentoResponse buscarPorPedido(UUID pedidoId) {
        SolicitacaoCancelamento solicitacao = cancelamentoRepository
                .findByPedidoId(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhuma solicitação encontrada para o pedido: " + pedidoId));
        return toResponse(solicitacao);
    }

    @Transactional(readOnly = true)
    public List<CancelamentoResponse> listarPorStatus(StatusCancelamento status) {
        if (status == null) {
            return cancelamentoRepository.findAll()
                    .stream().map(this::toResponse).toList();
        }
        return cancelamentoRepository.findByStatus(status)
                .stream().map(this::toResponse).toList();
    }

    private SolicitacaoCancelamento buscarSolicitacao(UUID id) {
        return cancelamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Solicitação de cancelamento não encontrada: " + id));
    }

    private CancelamentoResponse toResponse(SolicitacaoCancelamento s) {
        return CancelamentoResponse.builder()
                .id(s.getId())
                .pedidoId(s.getPedido().getId())
                .numeroPedido(s.getPedido().getNumeroPedido())
                .clienteNome(s.getPedido().getClienteNome())
                .motivo(s.getMotivo())
                .descricao(s.getDescricao())
                .statusPedidoMomento(s.getStatusPedidoMomento())
                .status(s.getStatus())
                .bloqueadoMotivo(s.getBloqueadoMotivo())
                .requerRetornoCD(s.isRequerRetornoCD())
                .dataResolucao(s.getDataResolucao())
                .criadoEm(s.getCriadoEm())
                .atualizadoEm(s.getAtualizadoEm())
                .build();
    }

}