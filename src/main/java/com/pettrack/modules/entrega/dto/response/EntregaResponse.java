package com.pettrack.modules.entrega.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import com.pettrack.modules.entrega.domain.enums.StatusEntrega;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class EntregaResponse {

    private UUID id;
    private UUID pedidoId;
    private String numeroPedido;
    private String clienteNome;
    private String enderecoEntrega;
    private String filialNome;
    private String veiculoPlaca;
    private SubregiaoFilial subregiao;
    private StatusEntrega status;
    private LocalDateTime dataDespacho;
    private LocalDateTime dataEntrega;
    private String observacoes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}