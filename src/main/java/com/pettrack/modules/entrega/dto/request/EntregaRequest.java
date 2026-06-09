package com.pettrack.modules.entrega.dto.request;

import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class EntregaRequest {

    @NotNull(message = "Pedido é obrigatório")
    private UUID pedidoId;

    @NotNull(message = "Subregião é obrigatória")
    private SubregiaoFilial subregiao;

    @NotNull(message = "Veículo é obrigatório")
    private UUID veiculoId;

    private String observacoes;

}