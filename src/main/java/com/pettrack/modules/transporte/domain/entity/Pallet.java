package com.pettrack.modules.transporte.domain.entity;

import com.pettrack.modules.ecommerce.domain.entity.Gaiola;
import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transporte.domain.enums.StatusPallet;
import com.pettrack.modules.transportadora.domain.entity.Transportadora;
import com.pettrack.modules.usuario.domain.entity.Usuario;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pallet extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_pallet", nullable = false, unique = true, length = 30)
    private String codigoPallet;

    @Enumerated(EnumType.STRING)
    @Column(name = "regiao_destino", nullable = false)
    private RegiaoCD regiaoDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transportadora_id")
    private Transportadora transportadora;

    @Column(name = "peso_maximo_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal pesoMaximoKg;

    @Builder.Default
    @Column(name = "peso_atual_kg", precision = 10, scale = 3)
    private BigDecimal pesoAtualKg = BigDecimal.ZERO;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPallet status = StatusPallet.EM_MONTAGEM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validado_por_id")
    private Usuario validadoPor;

    @Builder.Default
    @OneToMany(mappedBy = "pallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PalletGaiola> gaiolas = new ArrayList<>();

    @Column(name = "observacoes", length = 300)
    private String observacoes;

    public boolean aceitaPeso(BigDecimal pesoKg) {
        return pesoAtualKg.add(pesoKg).compareTo(pesoMaximoKg) <= 0;
    }

    public double percentualOcupacao() {
        if (pesoMaximoKg.compareTo(BigDecimal.ZERO) == 0) return 0;
        return pesoAtualKg
                .divide(pesoMaximoKg, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

}