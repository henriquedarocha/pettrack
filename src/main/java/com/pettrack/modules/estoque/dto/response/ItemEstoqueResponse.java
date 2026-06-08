package com.pettrack.modules.estoque.dto.response;

import com.pettrack.modules.estoque.domain.enums.StatusItemEstoque;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ItemEstoqueResponse {

    private UUID id;
    private UUID produtoId;
    private String produtoNome;
    private String produtoSku;
    private UUID baiaId;
    private String baiaCodigo;
    private String numeroLote;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private Integer quantidade;
    private String numeroSerie;
    private String notaFiscalEntrada;
    private StatusItemEstoque status;
    private boolean proximoDoVencimento;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

}