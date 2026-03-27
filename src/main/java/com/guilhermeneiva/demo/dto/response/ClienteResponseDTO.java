package com.guilhermeneiva.demo.dto.response;

import com.guilhermeneiva.demo.model.entity.Cliente;

public record ClienteResponseDTO(
        Long id,
        String CPF,
        String nome,
        String data_de_nascimento,
        String telefone,
        String email,
        String role

) {
    public ClienteResponseDTO(Cliente cliente) {
        this(cliente.getId(), cliente.getCPF(), cliente.getNome(), cliente.getData_de_nascimento(), cliente.getEmail(), cliente.getTelefone(), String.valueOf(cliente.getRole()));
    }
}
