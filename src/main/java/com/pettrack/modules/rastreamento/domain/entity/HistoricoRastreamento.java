package com.pettrack.modules.rastreamento.domain.entity;

import com.pettrack.modules.ecommerce.domain.entity.Pedido;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.usuario.domain.entity.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_rastreamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoRastreamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_anterior")
    private StatusPedido statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_novo", nullable = false)
    private StatusPedido statusNovo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario responsavel;

    @Column(name = "localizacao", length = 150)
    private String localizacao;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Builder.Default
    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm = LocalDateTime.now();

}