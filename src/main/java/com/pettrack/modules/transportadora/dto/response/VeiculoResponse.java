package com.pettrack.modules.transportadora.dto.response;

import com.pettrack.modules.transportadora.domain.enums.TipoVeiculo;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class VeiculoResponse {

    private UUID id;
    private String placa;
    private TipoVeiculo tipo;
    private UUID transportadoraId;
    private String transportadoraNome;
    private BigDecimal capacidadeKg;
    private Integer capacidadePallets;
    private boolean disponivel;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}