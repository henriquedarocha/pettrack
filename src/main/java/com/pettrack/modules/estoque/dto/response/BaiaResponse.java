package com.pettrack.modules.estoque.dto.response;

import com.pettrack.modules.estoque.domain.enums.StatusBaia;
import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
public class BaiaResponse {

    private UUID id;
    private String codigo;
    private String descricao;
    private CategoriaProduto categoriaProduto;
    private TipoArmazenamento tipoArmazenamento;
    private BigDecimal capacidadeKg;
    private Integer capacidadeUnidades;
    private BigDecimal pesoAtualKg;
    private Integer unidadesAtuais;
    private StatusBaia status;
    private Double temperaturaMinima;
    private Double temperaturaMaxima;
    private double percentualOcupacao;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}