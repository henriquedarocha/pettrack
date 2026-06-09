package com.pettrack.modules.transporte.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "viagem_pallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemPallet {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "viagem_id", nullable = false)
    private Viagem viagem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pallet_id", nullable = false)
    private Pallet pallet;

}