package com.pettrack.modules.entrega.domain.entity;

import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import com.pettrack.modules.entrega.domain.enums.StatusEntrega;
import com.pettrack.modules.filial.domain.entity.Filial;
import com.pettrack.modules.transportadora.domain.entity.Veiculo;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "entregas_finais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntregaFinal extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "subregiao", nullable = false)
    private SubregiaoFilial subregiao;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEntrega status = StatusEntrega.AGUARDANDO_DESPACHO;

    @Column(name = "data_despacho")
    private LocalDateTime dataDespacho;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "observacoes", length = 300)
    private String observacoes;

}