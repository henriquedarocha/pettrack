package com.pettrack.modules.transporte.controller;

import com.pettrack.modules.transporte.domain.enums.StatusPallet;
import com.pettrack.modules.transporte.domain.enums.StatusViagem;
import com.pettrack.modules.transporte.dto.request.AdicionarGaiolaPalletRequest;
import com.pettrack.modules.transporte.dto.request.PalletRequest;
import com.pettrack.modules.transporte.dto.request.ViagemRequest;
import com.pettrack.modules.transporte.dto.response.PalletResponse;
import com.pettrack.modules.transporte.dto.response.ViagemResponse;
import com.pettrack.modules.transporte.service.TransporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transporte")
@RequiredArgsConstructor
public class TransporteController {

    private final TransporteService transporteService;

    @PostMapping("/pallets")
    public ResponseEntity<PalletResponse> criarPallet(
            @RequestBody @Valid PalletRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transporteService.criarPallet(request));
    }

    @GetMapping("/pallets/{id}")
    public ResponseEntity<PalletResponse> buscarPalletPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(transporteService.buscarPorId(id));
    }

    @GetMapping("/pallets")
    public ResponseEntity<List<PalletResponse>> listarPallets(
            @RequestParam(required = false) StatusPallet status) {
        return ResponseEntity.ok(transporteService.listarPorStatus(status));
    }

    @PatchMapping("/pallets/{id}/gaiola")
    public ResponseEntity<PalletResponse> adicionarGaiola(
            @PathVariable UUID id,
            @RequestBody @Valid AdicionarGaiolaPalletRequest request) {
        return ResponseEntity.ok(transporteService.adicionarGaiola(id, request));
    }

    @PatchMapping("/pallets/{id}/validar")
    public ResponseEntity<PalletResponse> validarPallet(
            @PathVariable UUID id,
            @RequestParam UUID usuarioId) {
        return ResponseEntity.ok(transporteService.validarPallet(id, usuarioId));
    }

    @PostMapping("/viagens")
    public ResponseEntity<ViagemResponse> criarViagem(
            @RequestBody @Valid ViagemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transporteService.criarViagem(request));
    }

    @GetMapping("/viagens")
    public ResponseEntity<List<ViagemResponse>> listarViagens(
            @RequestParam(required = false) StatusViagem status) {
        return ResponseEntity.ok(transporteService.listarPorStatus(status));
    }

    @PatchMapping("/viagens/{id}/pallet/{palletId}")
    public ResponseEntity<ViagemResponse> adicionarPallet(
            @PathVariable UUID id,
            @PathVariable UUID palletId) {
        return ResponseEntity.ok(transporteService.adicionarPallet(id, palletId));
    }

    @PatchMapping("/viagens/{id}/partir")
    public ResponseEntity<ViagemResponse> partirViagem(@PathVariable UUID id) {
        return ResponseEntity.ok(transporteService.partirViagem(id));
    }

    @PatchMapping("/viagens/{id}/chegada")
    public ResponseEntity<ViagemResponse> registrarChegada(@PathVariable UUID id) {
        return ResponseEntity.ok(transporteService.registrarChegada(id));
    }

}