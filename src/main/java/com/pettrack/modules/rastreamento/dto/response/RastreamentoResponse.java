package com.pettrack.modules.rastreamento.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class RastreamentoResponse {

    private UUID id;
    private UUID pedidoId;
    private String numeroPedido;
    private StatusPedido statusAnterior;
    private StatusPedido statusNovo;
    private String responsavelNome;
    private String localizacao;
    private String observacao;
    private LocalDateTime registradoEm;

}