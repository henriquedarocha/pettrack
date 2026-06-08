package com.pettrack.modules.estoque.controller;

import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.estoque.dto.request.BaiaRequest;
import com.pettrack.modules.estoque.dto.request.ItemEstoqueRequest;
import com.pettrack.modules.estoque.dto.response.BaiaResponse;
import com.pettrack.modules.estoque.dto.response.ItemEstoqueResponse;
import com.pettrack.modules.estoque.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    // ==================== BAIAS ====================

    @PostMapping("/baias")
    public ResponseEntity<BaiaResponse> cadastrarBaia(@RequestBody @Valid BaiaRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.cadastrarBaia(request));
    }

    @GetMapping("/baias")
    public ResponseEntity<List<BaiaResponse>> listarBaias(){
        return ResponseEntity.ok(estoqueService.listarBaias());
    }

    @GetMapping("/baias/{id}")
    public ResponseEntity<BaiaResponse> buscarBaiaPorId(@PathVariable UUID id){
        return ResponseEntity.ok(estoqueService.buscarBaiaPorId(id));
    }

    @GetMapping("/baias/codigo/{codigo}")
    public ResponseEntity<BaiaResponse> buscarBaiaPorCodigo(@PathVariable String codigo){
        return ResponseEntity.ok(estoqueService.buscarBaiaPorCodigo(codigo));
    }

    @PatchMapping("/baias/{id}/status")
    public ResponseEntity<BaiaResponse> atualizarStatusBaia(
            @PathVariable UUID id,
            @RequestParam StatusBaia novoStatus) {
        return ResponseEntity.ok(estoqueService.atualizarStatusBaia(id, novoStatus));
    }

    // ==================== ITENS ====================

    @PostMapping("/itens")
    public ResponseEntity<ItemEstoqueResponse> entrarEstoque(
            @RequestBody @Valid ItemEstoqueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estoqueService.entrarEstoque(request));
    }

    @GetMapping("/itens/baia/{baiaId}")
    public ResponseEntity<List<ItemEstoqueResponse>> listarItensPorBaia(@PathVariable UUID baiaId) {
        return ResponseEntity.ok(estoqueService.listarItensPorBaia(baiaId));
    }

    @GetMapping("/itens/produto/{produtoId}")
    public ResponseEntity<List<ItemEstoqueResponse>> listarItensPorProduto(
            @PathVariable UUID produtoId) {
        return ResponseEntity.ok(estoqueService.listarItensPorProduto(produtoId));
    }

    @GetMapping("/itens/produto/{produtoId}/total")
    public ResponseEntity<Integer> totalDisponivelPorProduto(@PathVariable UUID produtoId) {
        return ResponseEntity.ok(estoqueService.totalDisponivelPorProduto(produtoId));
    }

}