package com.pettrack.modules.filial.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.filial.domain.enums.StatusFilial;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class FilialResponse {

    private UUID id;
    private String nome;
    private RegiaoCD regiao;
    private String endereco;
    private String cep;
    private String cidade;
    private String uf;
    private String telefone;
    private String email;
    private StatusFilial status;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}