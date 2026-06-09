package com.pettrack.modules.ecommerce.dto.request;

import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarStatusPedidoRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido novoStatus;

    private String observacoes;

}