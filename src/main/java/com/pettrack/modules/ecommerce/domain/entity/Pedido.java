package com.pettrack.modules.ecommerce.domain.entity;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import com.pettrack.shared.audit.AuditEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pedido extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "numero_pedido", nullable = false, unique = true, length = 30)
    private String numeroPedido;

    @Column(name = "cliente_nome", nullable = false, length = 150)
    private String clienteNome;

    @Column(name = "cliente_cpf", nullable = false, length = 14)
    private String clienteCpf;

    @Column(name = "cliente_email", length = 100)
    private String clienteEmail;

    @Column(name = "cliente_telefone", length = 20)
    private String clienteTelefone;

    @Column(name = "endereco_entrega", nullable = false, length = 250)
    private String enderecoEntrega;

    @Column(name = "cep_entrega", nullable = false, length = 10)
    private String cepEntrega;

    @Column(name = "cidade_entrega", nullable = false, length = 100)
    private String cidadeEntrega;

    @Column(name = "uf_entrega", nullable = false, length = 2)
    private String ufEntrega;

    @Enumerated(EnumType.STRING)
    @Column(name = "regiao_cd", nullable = false)
    private RegiaoCD regiaoCD;

    @Enumerated(EnumType.STRING)
    @Column(name = "subregiao_filial")
    private SubregiaoFilial subregiaoFilial;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPedido status = StatusPedido.PEDIDO_CRIADO;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "peso_total_kg", precision = 10, scale = 3)
    private BigDecimal pesoTotalKg;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gaiola_id")
    private Gaiola gaiola;

    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

}