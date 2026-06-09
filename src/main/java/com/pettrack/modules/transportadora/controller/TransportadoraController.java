package com.pettrack.modules.transportadora.controller;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.dto.request.TransportadoraRequest;
import com.pettrack.modules.transportadora.dto.request.VeiculoRequest;
import com.pettrack.modules.transportadora.dto.response.TransportadoraResponse;
import com.pettrack.modules.transportadora.dto.response.VeiculoResponse;
import com.pettrack.modules.transportadora.service.TransportadoraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transportadoras")
@RequiredArgsConstructor
public class TransportadoraController {

    private final TransportadoraService transportadoraService;

    @PostMapping
    public ResponseEntity<TransportadoraResponse> cadastrar(
            @RequestBody @Valid TransportadoraRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportadoraService.cadastrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransportadoraResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(transportadoraService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TransportadoraResponse>> listarAtivas() {
        return ResponseEntity.ok(transportadoraService.listarAtivas());
    }

    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<List<TransportadoraResponse>> listarPorRegiao(
            @PathVariable RegiaoCD regiao) {
        return ResponseEntity.ok(transportadoraService.listarPorRegiao(regiao));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TransportadoraResponse> inativar(@PathVariable UUID id) {
        return ResponseEntity.ok(transportadoraService.inativar(id));
    }

    @PostMapping("/veiculos")
    public ResponseEntity<VeiculoResponse> cadastrarVeiculo(
            @RequestBody @Valid VeiculoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transportadoraService.cadastrarVeiculo(request));
    }

    @GetMapping("/veiculos/disponiveis")
    public ResponseEntity<List<VeiculoResponse>> listarVeiculosDisponiveis() {
        return ResponseEntity.ok(transportadoraService.listarVeiculosDisponiveis());
    }

    @PatchMapping("/veiculos/{id}/disponibilidade")
    public ResponseEntity<VeiculoResponse> atualizarDisponibilidade(
            @PathVariable UUID id,
            @RequestParam boolean disponivel) {
        return ResponseEntity.ok(transportadoraService.atualizarDisponibilidade(id, disponivel));
    }

}