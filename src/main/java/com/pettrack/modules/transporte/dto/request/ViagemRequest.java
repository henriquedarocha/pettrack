package com.pettrack.modules.transporte.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ViagemRequest {

    @NotBlank(message = "Código da viagem é obrigatório")
    @Size(max = 30, message = "Código deve ter no máximo 30 caracteres")
    private String codigoViagem;

    @NotNull(message = "Veículo é obrigatório")
    private UUID veiculoId;

    @NotNull(message = "Filial de destino é obrigatória")
    private UUID filialDestinoId;

    @Size(max = 300, message = "Observações devem ter no máximo 300 caracteres")
    private String observacoes;

}