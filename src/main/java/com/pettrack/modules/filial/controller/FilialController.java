package com.pettrack.modules.filial.controller;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.filial.domain.enums.StatusFilial;
import com.pettrack.modules.filial.dto.request.FilialRequest;
import com.pettrack.modules.filial.dto.response.FilialResponse;
import com.pettrack.modules.filial.service.FilialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/filiais")
@RequiredArgsConstructor
public class FilialController {

    private final FilialService filialService;

    @PostMapping
    public ResponseEntity<FilialResponse> cadastrar(
            @RequestBody @Valid FilialRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(filialService.cadastrar(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilialResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(filialService.buscarPorId(id));
    }

    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<FilialResponse> buscarPorRegiao(@PathVariable RegiaoCD regiao) {
        return ResponseEntity.ok(filialService.buscarPorRegiao(regiao));
    }

    @GetMapping
    public ResponseEntity<List<FilialResponse>> listarAtivas() {
        return ResponseEntity.ok(filialService.listarAtivas());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<FilialResponse> atualizarStatus(
            @PathVariable UUID id,
            @RequestParam StatusFilial novoStatus) {
        return ResponseEntity.ok(filialService.atualizarStatus(id, novoStatus));
    }

}