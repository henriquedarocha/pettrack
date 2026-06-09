package com.pettrack.modules.transporte.dto.request;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class PalletRequest {

    @NotBlank(message = "Código do pallet é obrigatório")
    @Size(max = 30, message = "Código deve ter no máximo 30 caracteres")
    private String codigoPallet;

    @NotNull(message = "Região de destino é obrigatória")
    private RegiaoCD regiaoDestino;

    @NotNull(message = "Peso máximo é obrigatório")
    @DecimalMin(value = "0.001", message = "Peso máximo deve ser maior que zero")
    private BigDecimal pesoMaximoKg;

    private UUID transportadoraId;

    @Size(max = 300, message = "Observações devem ter no máximo 300 caracteres")
    private String observacoes;

}