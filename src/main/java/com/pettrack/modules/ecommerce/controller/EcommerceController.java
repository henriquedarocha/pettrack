package com.pettrack.modules.ecommerce.controller;

import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.dto.request.PedidoRequest;
import com.pettrack.modules.ecommerce.dto.response.GaiolaResponse;
import com.pettrack.modules.ecommerce.dto.response.PedidoResponse;
import com.pettrack.modules.ecommerce.service.EcommerceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ecommerce")
@RequiredArgsConstructor
public class EcommerceController {

    private final EcommerceService ecommerceService;

    // ==================== PEDIDOS ====================

    @PostMapping("/pedidos")
    public ResponseEntity<PedidoResponse> criarPedido(
            @RequestBody @Valid PedidoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ecommerceService.criarPedido(request));
    }

    @PatchMapping("/pedidos/{id}/confirmar-pagamento")
    public ResponseEntity<PedidoResponse> confirmarPagamento(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.confirmarPagamento(id));
    }

    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.buscarPorId(id));
    }

    @GetMapping("/pedidos/numero/{numeroPedido}")
    public ResponseEntity<PedidoResponse> buscarPorNumero(@PathVariable String numeroPedido) {
        return ResponseEntity.ok(ecommerceService.buscarPorNumero(numeroPedido));
    }

    @GetMapping("/pedidos")
    public ResponseEntity<List<PedidoResponse>> listarPorStatus(
            @RequestParam(required = false) StatusPedido status) {
        return ResponseEntity.ok(ecommerceService.listarPorStatus(status));
    }

    @PatchMapping("/pedidos/{id}/separar")
    public ResponseEntity<PedidoResponse> separarPedido(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.separarPedido(id));
    }

    @PatchMapping("/pedidos/{id}/embalar")
    public ResponseEntity<PedidoResponse> embalarPedido(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.embalarPedido(id));
    }

    @PatchMapping("/pedidos/{id}/gaiola")
    public ResponseEntity<PedidoResponse> adicionarNaGaiola(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.adicionarNaGaiola(id));
    }

    @PatchMapping("/pedidos/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.cancelarPedido(id));
    }

    // ==================== GAIOLAS ====================

    @GetMapping("/gaiolas")
    public ResponseEntity<List<GaiolaResponse>> listarGaiolas() {
        return ResponseEntity.ok(ecommerceService.listarGaiolas());
    }

    @PatchMapping("/gaiolas/{id}/fechar")
    public ResponseEntity<GaiolaResponse> fecharGaiola(@PathVariable UUID id) {
        return ResponseEntity.ok(ecommerceService.fecharGaiola(id));
    }

}