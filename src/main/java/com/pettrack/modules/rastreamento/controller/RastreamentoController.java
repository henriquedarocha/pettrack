package com.pettrack.modules.rastreamento.controller;

import com.pettrack.modules.rastreamento.dto.response.RastreamentoResponse;
import com.pettrack.modules.rastreamento.service.RastreamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rastreamento")
@RequiredArgsConstructor
public class RastreamentoController {

    private final RastreamentoService rastreamentoService;

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<RastreamentoResponse>> buscarPorPedido(
            @PathVariable UUID pedidoId) {
        return ResponseEntity.ok(rastreamentoService.buscarHistoricoPorPedido(pedidoId));
    }

    @GetMapping("/pedido/numero/{numeroPedido}")
    public ResponseEntity<List<RastreamentoResponse>> buscarPorNumeroPedido(
            @PathVariable String numeroPedido) {
        return ResponseEntity.ok(rastreamentoService.buscarHistoricoPorNumeroPedido(numeroPedido));
    }

}