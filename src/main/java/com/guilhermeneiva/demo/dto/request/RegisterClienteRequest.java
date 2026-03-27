package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterClienteRequest(
        @NotBlank(message = "Nome deve ser obrigatório") String nome,
        @NotBlank(message = "Email deve ser obrigatório") String email,
        @NotBlank(message = "Data de nascimento é obrigatória") String data_de_nascimento,
        @NotBlank(message = "CPF não pode ser vazio") String CPF,
        @NotBlank(message = "Telefone é obrigatório") String telefone,
        @NotBlank(message = "Senha é obrigatória") String senha
) {
}
