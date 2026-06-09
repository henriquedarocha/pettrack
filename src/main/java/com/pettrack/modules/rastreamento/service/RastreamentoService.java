package com.pettrack.modules.rastreamento.service;

import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.repository.PedidoRepository;
import com.pettrack.modules.rastreamento.domain.entity.HistoricoRastreamento;
import com.pettrack.modules.rastreamento.dto.response.RastreamentoResponse;
import com.pettrack.modules.rastreamento.repository.RastreamentoRepository;
import com.pettrack.modules.usuario.domain.entity.Usuario;
import com.pettrack.shared.exception.RecursoNaoEncontradoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RastreamentoService {

    private final RastreamentoRepository rastreamentoRepository;
    private final PedidoRepository pedidoRepository;

    @Transactional
    public void registrar(Pedido pedido, StatusPedido statusAnterior,
                          StatusPedido statusNovo, Usuario responsavel,
                          String localizacao, String observacao) {
        HistoricoRastreamento historico = HistoricoRastreamento.builder()
                .pedido(pedido)
                .statusAnterior(statusAnterior)
                .statusNovo(statusNovo)
                .responsavel(responsavel)
                .localizacao(localizacao)
                .observacao(observacao)
                .build();

        rastreamentoRepository.save(historico);

        log.info("Rastreamento — Pedido: {}, {} → {}, Local: {}",
                pedido.getNumeroPedido(), statusAnterior, statusNovo, localizacao);
    }

    @Transactional(readOnly = true)
    public List<RastreamentoResponse> buscarHistoricoPorPedido(UUID pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido não encontrado: " + pedidoId));

        return rastreamentoRepository
                .findByPedidoIdOrderByRegistradoEmAsc(pedidoId)
                .stream()
                .map(h -> toResponse(h, pedido))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RastreamentoResponse> buscarHistoricoPorNumeroPedido(String numeroPedido) {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Pedido não encontrado: " + numeroPedido));

        return rastreamentoRepository
                .findByPedidoIdOrderByRegistradoEmAsc(pedido.getId())
                .stream()
                .map(h -> toResponse(h, pedido))
                .toList();
    }

    private RastreamentoResponse toResponse(HistoricoRastreamento h, Pedido pedido) {
        return RastreamentoResponse.builder()
                .id(h.getId())
                .pedidoId(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .statusAnterior(h.getStatusAnterior())
                .statusNovo(h.getStatusNovo())
                .responsavelNome(h.getResponsavel() != null
                        ? h.getResponsavel().getNome() : "sistema")
                .localizacao(h.getLocalizacao())
                .observacao(h.getObservacao())
                .registradoEm(h.getRegistradoEm())
                .build();
    }

}