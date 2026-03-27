package com.guilhermeneiva.demo.dto.response;

import com.guilhermeneiva.demo.model.entity.Barbeiro;

public record BarbeiroResponseDTO(
        Long id,
        String nome,
        String CPF,
        String telefone,
        String especialidade,
        String email
) {
    public BarbeiroResponseDTO(Barbeiro barbeiro) {
        this(barbeiro.getId(), barbeiro.getNome(), barbeiro.getCPF(), barbeiro.getTelefone(), barbeiro.getEspecialidade(), barbeiro.getEmail());
    }
}
