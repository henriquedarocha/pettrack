package com.pettrack.modules.cancelamento.dto.request;

import com.pettrack.modules.cancelamento.domain.enums.MotivoCancelamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CancelamentoRequest {

    @NotNull(message = "Pedido é obrigatório")
    private UUID pedidoId;

    @NotNull(message = "Motivo é obrigatório")
    private MotivoCancelamento motivo;

    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres")
    private String descricao;

}