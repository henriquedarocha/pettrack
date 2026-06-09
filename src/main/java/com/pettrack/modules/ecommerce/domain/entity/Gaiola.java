package com.pettrack.modules.ecommerce.domain.entity;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusGaiola;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "gaiolas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Gaiola extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "regiao_cd", nullable = false)
    private RegiaoCD regiaoCD;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusGaiola status = StatusGaiola.ABERTA;

    @Builder.Default
    @OneToMany(mappedBy = "gaiola", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pedido> pedidos = new ArrayList<>();

}