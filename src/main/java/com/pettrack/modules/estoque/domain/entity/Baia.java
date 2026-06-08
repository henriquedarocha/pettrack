package com.pettrack.modules.estoque.domain.entity;

import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "baias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Baia extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria_produto", nullable = false)
    private CategoriaProduto categoriaProduto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_armazenamento", nullable = false)
    private TipoArmazenamento tipoArmazenamento;

    @Column(name = "capacidade_kg", nullable = false, precision = 10, scale = 3)
    private BigDecimal capacidadeKg;

    @Column(name = "capacidade_unidades", nullable = false)
    private Integer capacidadeUnidades;

    @Builder.Default
    @Column(name = "peso_atual_kg", precision = 10, scale = 3)
    private BigDecimal pesoAtualKg = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "unidades_atuais")
    private Integer unidadesAtuais = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusBaia status = StatusBaia.DISPONIVEL;

    @Column(name = "temperatura_minima")
    private Double temperaturaMinima;

    @Column(name = "temperatura_maxima")
    private Double temperaturaMaxima;

    public boolean temCapacidade(BigDecimal pesoKg, int unidades) {
        return pesoAtualKg.add(pesoKg).compareTo(capacidadeKg) <= 0
                && (unidadesAtuais + unidades) <= capacidadeUnidades;
    }

    public double percentualOcupacao() {
        if (capacidadeKg.compareTo(BigDecimal.ZERO) == 0) return 0;
        return pesoAtualKg
                .divide(capacidadeKg, 4, java.math.RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }
}