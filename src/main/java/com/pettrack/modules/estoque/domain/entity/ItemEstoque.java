package com.pettrack.modules.estoque.domain.entity;

import com.pettrack.modules.estoque.domain.enums.StatusItemEstoque;
import com.pettrack.modules.produto.domain.entity.Produto;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "itens_estoque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEstoque extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "baia_id", nullable = false)
    private Baia baia;

    @Column(name = "numero_lote", nullable = false, length = 50)
    private String numeroLote;

    @Column(name = "data_fabricacao")
    private LocalDate dataFabricacao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(name = "numero_serie", length = 100)
    private String numeroSerie;

    @Column(name = "nota_fiscal_entrada", length = 50)
    private String notaFiscalEntrada;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusItemEstoque status = StatusItemEstoque.DISPONIVEL;

    public boolean estaVencido() {
        return dataValidade != null && LocalDate.now().isAfter(dataValidade);
    }

    public boolean venceEm(int dias) {
        return dataValidade != null && LocalDate.now().plusDays(dias).isAfter(dataValidade) && !estaVencido();
    }

}