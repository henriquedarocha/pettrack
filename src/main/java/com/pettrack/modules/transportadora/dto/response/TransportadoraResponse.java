package com.pettrack.modules.transportadora.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.domain.enums.TipoTransportadora;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Builder
public class TransportadoraResponse {

    private UUID id;
    private String nome;
    private String cnpj;
    private TipoTransportadora tipo;
    private Set<RegiaoCD> regioesAtendidas;
    private String telefone;
    private String emailContato;
    private boolean ativa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}