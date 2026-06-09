package com.pettrack.modules.transporte.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AdicionarGaiolaPalletRequest {

    @NotNull(message = "Gaiola é obrigatória")
    private UUID gaiolaId;

}