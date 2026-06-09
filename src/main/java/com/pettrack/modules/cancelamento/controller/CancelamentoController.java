package com.pettrack.modules.cancelamento.controller;

import com.pettrack.modules.cancelamento.domain.enums.StatusCancelamento;
import com.pettrack.modules.cancelamento.dto.request.CancelamentoRequest;
import com.pettrack.modules.cancelamento.dto.response.CancelamentoResponse;
import com.pettrack.modules.cancelamento.service.CancelamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cancelamentos")
@RequiredArgsConstructor
public class CancelamentoController {

    private final CancelamentoService cancelamentoService;

    @PostMapping
    public ResponseEntity<CancelamentoResponse> solicitarCancelamento(
            @RequestBody @Valid CancelamentoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cancelamentoService.solicitarCancelamento(request));
    }

    @PatchMapping("/{id}/confirmar-retorno")
    public ResponseEntity<CancelamentoResponse> confirmarRetornoCD(@PathVariable UUID id) {
        return ResponseEntity.ok(cancelamentoService.confirmarRetornoCD(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<CancelamentoResponse> buscarPorPedido(@PathVariable UUID pedidoId) {
        return ResponseEntity.ok(cancelamentoService.buscarPorPedido(pedidoId));
    }

    @GetMapping
    public ResponseEntity<List<CancelamentoResponse>> listarPorStatus(
            @RequestParam(required = false) StatusCancelamento status) {
        return ResponseEntity.ok(cancelamentoService.listarPorStatus(status));
    }

}