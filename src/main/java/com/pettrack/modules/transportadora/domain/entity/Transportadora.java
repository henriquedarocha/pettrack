package com.pettrack.modules.transportadora.domain.entity;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.domain.enums.TipoTransportadora;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "transportadoras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transportadora extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 20)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransportadora tipo;

    @Builder.Default
    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "transportadora_regioes",
            joinColumns = @JoinColumn(name = "transportadora_id")
    )
    @Column(name = "regiao")
    private Set<RegiaoCD> regioesAtendidas = new HashSet<>();

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email_contato", length = 100)
    private String emailContato;

    @Builder.Default
    @Column(nullable = false)
    private boolean ativa = true;

}