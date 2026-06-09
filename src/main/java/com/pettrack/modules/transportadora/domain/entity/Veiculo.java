package com.pettrack.modules.transportadora.domain.entity;

import com.pettrack.modules.transportadora.domain.enums.TipoVeiculo;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Veiculo extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoVeiculo tipo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transportadora_id", nullable = false)
    private Transportadora transportadora;

    @Column(name = "capacidade_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal capacidadeKg;

    @Column(name = "capacidade_pallets")
    private Integer capacidadePallets;

    @Builder.Default
    @Column(nullable = false)
    private boolean disponivel = true;

}