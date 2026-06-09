package com.pettrack.modules.filial.dto.request;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilialRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @NotNull(message = "Região é obrigatória")
    private RegiaoCD regiao;

    @NotBlank(message = "Endereço é obrigatório")
    private String endereco;

    @NotBlank(message = "CEP é obrigatório")
    private String cep;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "UF é obrigatória")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
    private String uf;

    private String telefone;
    private String email;

}