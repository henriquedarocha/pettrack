package com.pettrack.modules.estoque.dto.request;

import com.pettrack.modules.produto.domain.enums.CategoriaProduto;
import com.pettrack.modules.produto.domain.enums.TipoArmazenamento;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class BaiaRequest {

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 10, message = "Código deve ter no máximo 10 caracteres")
    private String codigo;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    @NotNull(message = "Categoria do produto é obrigatória")
    private CategoriaProduto categoriaProduto;

    @NotNull(message = "Tipo de armazenamento é obrigatório")
    private TipoArmazenamento tipoArmazenamento;

    @NotNull(message = "Capacidade em kg é obrigatória")
    @DecimalMin(value = "0.001", message = "Capacidade deve ser maior que zero")
    private BigDecimal capacidadeKg;

    @NotNull(message = "Capacidade em unidades é obrigatória")
    @Positive(message = "Capacidade em unidades deve ser maior que zero")
    private Integer capacidadeUnidades;

    private Double temperaturaMinima;

    private Double temperaturaMaxima;

}