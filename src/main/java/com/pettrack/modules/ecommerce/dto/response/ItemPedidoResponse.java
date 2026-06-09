package com.pettrack.modules.ecommerce.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
public class ItemPedidoResponse {

    private UUID id;
    private UUID produtoId;
    private String produtoNome;
    private String produtoSku;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal precoTotal;
    private boolean separado;

}