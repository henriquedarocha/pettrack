package com.pettrack.modules.transportadora.dto.request;

import com.pettrack.modules.transportadora.domain.enums.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class VeiculoRequest {

    @NotBlank(message = "Placa é obrigatória")
    @Size(max = 20, message = "Placa deve ter no máximo 20 caracteres")
    private String placa;

    @NotNull(message = "Tipo é obrigatório")
    private TipoVeiculo tipo;

    @NotNull(message = "Transportadora é obrigatória")
    private UUID transportadoraId;

    @NotNull(message = "Capacidade em kg é obrigatória")
    @Positive(message = "Capacidade deve ser maior que zero")
    private BigDecimal capacidadeKg;

    @Positive(message = "Capacidade de pallets deve ser maior que zero")
    private Integer capacidadePallets;

}