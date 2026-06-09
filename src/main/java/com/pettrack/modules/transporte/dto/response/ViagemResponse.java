package com.pettrack.modules.transporte.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transporte.domain.enums.StatusViagem;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ViagemResponse {

    private UUID id;
    private String codigoViagem;
    private String veiculoPlaca;
    private String transportadoraNome;
    private String filialDestinoNome;
    private RegiaoCD regiaoCD;
    private StatusViagem status;
    private LocalDateTime dataSaida;
    private LocalDateTime dataChegada;
    private String observacoes;
    private int totalPallets;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}