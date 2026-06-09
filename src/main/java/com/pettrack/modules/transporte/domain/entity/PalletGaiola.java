package com.pettrack.modules.transporte.domain.entity;

import com.pettrack.modules.ecommerce.domain.entity.Gaiola;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "pallet_gaiolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PalletGaiola {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pallet_id", nullable = false)
    private Pallet pallet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gaiola_id", nullable = false)
    private Gaiola gaiola;

}