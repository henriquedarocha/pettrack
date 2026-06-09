package com.pettrack.modules.cancelamento.domain.entity;

import com.pettrack.modules.cancelamento.domain.enums.MotivoCancelamento;
import com.pettrack.modules.cancelamento.domain.enums.StatusCancelamento;
import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "solicitacoes_cancelamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitacaoCancelamento extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo", nullable = false)
    private MotivoCancelamento motivo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pedido_momento", nullable = false)
    private StatusPedido statusPedidoMomento;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCancelamento status = StatusCancelamento.SOLICITADO;

    @Column(name = "bloqueado_motivo", length = 300)
    private String bloqueadoMotivo;

    @Column(name = "data_resolucao")
    private LocalDateTime dataResolucao;

    @Column(name = "requer_retorno_cd")
    private boolean requerRetornoCD = false;

}