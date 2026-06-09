package com.pettrack.modules.cancelamento.dto.response;

import com.pettrack.modules.cancelamento.domain.enums.MotivoCancelamento;
import com.pettrack.modules.cancelamento.domain.enums.StatusCancelamento;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CancelamentoResponse {

    private UUID id;
    private UUID pedidoId;
    private String numeroPedido;
    private String clienteNome;
    private MotivoCancelamento motivo;
    private String descricao;
    private StatusPedido statusPedidoMomento;
    private StatusCancelamento status;
    private String bloqueadoMotivo;
    private boolean requerRetornoCD;
    private LocalDateTime dataResolucao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}