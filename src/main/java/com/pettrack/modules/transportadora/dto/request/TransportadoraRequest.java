package com.pettrack.modules.transportadora.dto.request;

import com.pettrack.modules.ecommerce.domain.enums.RegiaoCD;
import com.pettrack.modules.transportadora.domain.enums.TipoTransportadora;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TransportadoraRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String nome;

    @Size(max = 20, message = "CNPJ deve ter no máximo 20 caracteres")
    private String cnpj;

    @NotNull(message = "Tipo é obrigatório")
    private TipoTransportadora tipo;

    @NotEmpty(message = "Pelo menos uma região deve ser informada")
    private Set<RegiaoCD> regioesAtendidas;

    private String telefone;

    private String emailContato;

}