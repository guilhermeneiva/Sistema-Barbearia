package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record BarbeiroRequestDTO(
        @NotBlank(message = "Nome não pode ser vazio")
        String nome,
        @NotBlank(message = "CPF não pode ser vazio")
        String CPF,
        @NotBlank(message = "Telefone não pode ser vazio")
        String telefone,
        @NotBlank(message = "Especialidade não pode ser vazia")
        String especialidade,
        @NotBlank
        String email,
        @NotBlank
        String senha
) {
}
