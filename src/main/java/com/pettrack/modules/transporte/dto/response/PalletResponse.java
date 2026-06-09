package com.pettrack.modules.transporte.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transporte.domain.enums.StatusPallet;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PalletResponse {

    private UUID id;
    private String codigoPallet;
    private RegiaoCD regiaoDestino;
    private String transportadoraNome;
    private BigDecimal pesoMaximoKg;
    private BigDecimal pesoAtualKg;
    private double percentualOcupacao;
    private StatusPallet status;
    private String validadoPorNome;
    private String observacoes;
    private int totalGaiolas;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}