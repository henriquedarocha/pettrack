package com.pettrack.modules.transporte.domain.entity;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.filial.domain.entity.Filial;
import com.pettrack.modules.transporte.domain.enums.StatusViagem;
import com.pettrack.modules.transportadora.domain.entity.Veiculo;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "viagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Viagem extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_viagem", nullable = false, unique = true, length = 30)
    private String codigoViagem;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filial_destino_id", nullable = false)
    private Filial filialDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "regiao_cd", nullable = false)
    private RegiaoCD regiaoCD;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusViagem status = StatusViagem.AGUARDANDO_CARREGAMENTO;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    @Column(name = "data_chegada")
    private LocalDateTime dataChegada;

    @Column(name = "observacoes", length = 300)
    private String observacoes;

    @Builder.Default
    @OneToMany(mappedBy = "viagem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ViagemPallet> pallets = new ArrayList<>();

}