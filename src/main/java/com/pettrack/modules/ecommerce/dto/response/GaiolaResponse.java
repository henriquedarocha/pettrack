package com.pettrack.modules.ecommerce.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusGaiola;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class GaiolaResponse {

    private UUID id;
    private String codigo;
    private RegiaoCD regiaoCD;
    private StatusGaiola status;
    private int totalPedidos;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}