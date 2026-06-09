package com.pettrack.modules.entrega.service;

import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.repository.PedidoRepository;
import com.pettrack.modules.entrega.domain.entity.EntregaFinal;
import com.pettrack.modules.entrega.domain.enums.StatusEntrega;
import com.pettrack.modules.entrega.dto.request.EntregaRequest;
import com.pettrack.modules.entrega.dto.response.EntregaResponse;
import com.pettrack.modules.entrega.repository.EntregaRepository;
import com.pettrack.modules.filial.domain.entity.Filial;
import com.pettrack.modules.filial.repository.FilialRepository;
import com.pettrack.modules.rastreamento.service.RastreamentoService;
import com.pettrack.modules.transportadora.domain.entity.Veiculo;
import com.pettrack.modules.transportadora.repository.VeiculoRepository;
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
public class EntregaService {

    private final EntregaRepository entregaRepository;
    private final PedidoRepository pedidoRepository;
    private final FilialRepository filialRepository;
    private final VeiculoRepository veiculoRepository;
    private final RastreamentoService rastreamentoService;

    @Transactional
    public EntregaResponse criarEntrega(UUID filialId, EntregaRequest request) {
        Pedido pedido = pedidoRepository.findById(request.getPedidoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.RECEBIDO_NA_FILIAL &&
                pedido.getStatus() != StatusPedido.EM_SEPARACAO_FILIAL) {
            throw new NegocioException("Pedido não está disponível para entrega na filial");
        }

        Filial filial = filialRepository.findById(filialId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Filial não encontrada"));

        Veiculo veiculo = veiculoRepository.findById(request.getVeiculoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Veículo não encontrado"));

        if (!veiculo.isDisponivel()) {
            throw new NegocioException("Veículo não está disponível");
        }

        pedido.setSubregiaoFilial(request.getSubregiao());
        pedido.setStatus(StatusPedido.EM_SEPARACAO_FILIAL);
        pedidoRepository.save(pedido);

        EntregaFinal entrega = EntregaFinal.builder()
                .pedido(pedido)
                .filial(filial)
                .veiculo(veiculo)
                .subregiao(request.getSubregiao())
                .observacoes(request.getObservacoes())
                .build();

        rastreamentoService.registrar(pedido,
                StatusPedido.RECEBIDO_NA_FILIAL,
                StatusPedido.EM_SEPARACAO_FILIAL,
                null,
                filial.getNome(),
                "Pedido separado para subregião: " + request.getSubregiao());

        return toResponse(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse despacharEntrega(UUID entregaId) {
        EntregaFinal entrega = buscarEntrega(entregaId);

        if (entrega.getStatus() != StatusEntrega.AGUARDANDO_DESPACHO) {
            throw new NegocioException("Entrega não está aguardando despacho");
        }

        entrega.setStatus(StatusEntrega.EM_ROTA);
        entrega.setDataDespacho(LocalDateTime.now());

        Pedido pedido = entrega.getPedido();
        pedido.setStatus(StatusPedido.EM_ROTA_ENTREGA);
        pedidoRepository.save(pedido);

        entrega.getVeiculo().setDisponivel(false);
        veiculoRepository.save(entrega.getVeiculo());

        rastreamentoService.registrar(pedido,
                StatusPedido.EM_SEPARACAO_FILIAL,
                StatusPedido.EM_ROTA_ENTREGA,
                null,
                entrega.getFilial().getNome(),
                "Van despachada — Placa: " + entrega.getVeiculo().getPlaca());

        log.info("Entrega despachada — Pedido: {}, Van: {}, Subregião: {}",
                pedido.getNumeroPedido(),
                entrega.getVeiculo().getPlaca(),
                entrega.getSubregiao());

        return toResponse(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse confirmarEntrega(UUID entregaId) {
        EntregaFinal entrega = buscarEntrega(entregaId);

        if (entrega.getStatus() != StatusEntrega.EM_ROTA) {
            throw new NegocioException("Entrega não está em rota");
        }

        entrega.setStatus(StatusEntrega.ENTREGUE);
        entrega.setDataEntrega(LocalDateTime.now());

        Pedido pedido = entrega.getPedido();
        pedido.setStatus(StatusPedido.ENTREGUE);
        pedidoRepository.save(pedido);

        entrega.getVeiculo().setDisponivel(true);
        veiculoRepository.save(entrega.getVeiculo());

        rastreamentoService.registrar(pedido,
                StatusPedido.EM_ROTA_ENTREGA,
                StatusPedido.ENTREGUE,
                null,
                entrega.getPedido().getEnderecoEntrega(),
                "Entrega confirmada ao cliente: " + pedido.getClienteNome());

        log.info("Entrega confirmada — Pedido: {}, Cliente: {}",
                pedido.getNumeroPedido(), pedido.getClienteNome());

        return toResponse(entregaRepository.save(entrega));
    }

    @Transactional
    public EntregaResponse registrarTentativaFalha(UUID entregaId, String motivo) {
        EntregaFinal entrega = buscarEntrega(entregaId);

        if (entrega.getStatus() != StatusEntrega.EM_ROTA) {
            throw new NegocioException("Entrega não está em rota");
        }

        entrega.setStatus(StatusEntrega.TENTATIVA_FALHA);
        entrega.setObservacoes(motivo);

        Pedido pedido = entrega.getPedido();
        entrega.getVeiculo().setDisponivel(true);
        veiculoRepository.save(entrega.getVeiculo());

        rastreamentoService.registrar(pedido,
                StatusPedido.EM_ROTA_ENTREGA,
                StatusPedido.EM_SEPARACAO_FILIAL,
                null,
                entrega.getFilial().getNome(),
                "Tentativa falha: " + motivo);

        return toResponse(entregaRepository.save(entrega));
    }

    @Transactional(readOnly = true)
    public List<EntregaResponse> listarPorFilial(UUID filialId, StatusEntrega status) {
        if (status == null) {
            return entregaRepository.findAll().stream()
                    .filter(e -> e.getFilial().getId().equals(filialId))
                    .map(this::toResponse).toList();
        }
        return entregaRepository.findByFilialIdAndStatus(filialId, status)
                .stream().map(this::toResponse).toList();
    }

    private EntregaFinal buscarEntrega(UUID id) {
        return entregaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Entrega não encontrada: " + id));
    }

    private EntregaResponse toResponse(EntregaFinal e) {
        return EntregaResponse.builder()
                .id(e.getId())
                .pedidoId(e.getPedido().getId())
                .numeroPedido(e.getPedido().getNumeroPedido())
                .clienteNome(e.getPedido().getClienteNome())
                .enderecoEntrega(e.getPedido().getEnderecoEntrega())
                .filialNome(e.getFilial().getNome())
                .veiculoPlaca(e.getVeiculo() != null ? e.getVeiculo().getPlaca() : null)
                .subregiao(e.getSubregiao())
                .status(e.getStatus())
                .dataDespacho(e.getDataDespacho())
                .dataEntrega(e.getDataEntrega())
                .observacoes(e.getObservacoes())
                .criadoEm(e.getCriadoEm())
                .atualizadoEm(e.getAtualizadoEm())
                .build();
    }

}