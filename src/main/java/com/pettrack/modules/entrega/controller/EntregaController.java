package com.pettrack.modules.entrega.controller;

import com.pettrack.modules.entrega.domain.enums.StatusEntrega;
import com.pettrack.modules.entrega.dto.request.EntregaRequest;
import com.pettrack.modules.entrega.dto.response.EntregaResponse;
import com.pettrack.modules.entrega.service.EntregaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping("/filial/{filialId}")
    public ResponseEntity<EntregaResponse> criarEntrega(
            @PathVariable UUID filialId,
            @RequestBody @Valid EntregaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(entregaService.criarEntrega(filialId, request));
    }

    @PatchMapping("/{id}/despachar")
    public ResponseEntity<EntregaResponse> despacharEntrega(@PathVariable UUID id) {
        return ResponseEntity.ok(entregaService.despacharEntrega(id));
    }

    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<EntregaResponse> confirmarEntrega(@PathVariable UUID id) {
        return ResponseEntity.ok(entregaService.confirmarEntrega(id));
    }

    @PatchMapping("/{id}/tentativa-falha")
    public ResponseEntity<EntregaResponse> registrarTentativaFalha(
            @PathVariable UUID id,
            @RequestParam String motivo) {
        return ResponseEntity.ok(entregaService.registrarTentativaFalha(id, motivo));
    }

    @GetMapping("/filial/{filialId}")
    public ResponseEntity<List<EntregaResponse>> listarPorFilial(
            @PathVariable UUID filialId,
            @RequestParam(required = false) StatusEntrega status) {
        return ResponseEntity.ok(entregaService.listarPorFilial(filialId, status));
    }

}