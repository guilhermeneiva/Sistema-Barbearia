package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,
        @NotBlank(message = "Data de nascimento não pode ser vazia")
        String data_de_nascimento,
        @NotBlank(message = "Email não pode ser vazio")
        String email,
        @NotBlank(message = "Telefone não pode ser vazio")
        String telefone,
        @NotBlank(message = "CPF não pode ser vazio")
        String CPF,
        String senha

) {
}