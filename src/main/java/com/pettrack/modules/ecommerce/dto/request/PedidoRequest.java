package com.pettrack.modules.ecommerce.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    @NotBlank(message = "Número do pedido é obrigatório")
    @Size(max = 30, message = "Número do pedido deve ter no máximo 30 caracteres")
    private String numeroPedido;

    @NotBlank(message = "Nome do cliente é obrigatório")
    @Size(max = 150, message = "Nome deve ter no máximo 150 caracteres")
    private String clienteNome;

    @NotBlank(message = "CPF do cliente é obrigatório")
    @Size(min = 11, max = 14, message = "CPF inválido")
    private String clienteCpf;

    @Email(message = "Email inválido")
    private String clienteEmail;

    private String clienteTelefone;

    @NotBlank(message = "Endereço de entrega é obrigatório")
    private String enderecoEntrega;

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 10, message = "CEP inválido")
    private String cepEntrega;

    @NotBlank(message = "Cidade é obrigatória")
    private String cidadeEntrega;

    @NotBlank(message = "UF é obrigatória")
    @Size(min = 2, max = 2, message = "UF deve ter 2 caracteres")
    private String ufEntrega;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    @Valid
    @NotEmpty(message = "O pedido deve ter pelo menos um item")
    private List<ItemPedidoRequest> itens;

}