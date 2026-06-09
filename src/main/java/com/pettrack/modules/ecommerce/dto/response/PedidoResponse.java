package com.pettrack.modules.ecommerce.dto.response;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.ecommerce.domain.enums.StatusPedido;
import com.pettrack.modules.ecommerce.domain.enums.SubregiaoFilial;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class PedidoResponse {

    private UUID id;
    private String numeroPedido;
    private String clienteNome;
    private String clienteCpf;
    private String clienteEmail;
    private String clienteTelefone;
    private String enderecoEntrega;
    private String cepEntrega;
    private String cidadeEntrega;
    private String ufEntrega;
    private RegiaoCD regiaoCD;
    private SubregiaoFilial subregiaoFilial;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private BigDecimal pesoTotalKg;
    private String observacoes;
    private String gaiolaCodigo;
    private List<ItemPedidoResponse> itens;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}