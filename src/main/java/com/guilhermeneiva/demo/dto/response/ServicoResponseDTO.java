package com.guilhermeneiva.demo.dto.response;

import com.guilhermeneiva.demo.model.entity.Servico;

import java.math.BigDecimal;

public record ServicoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Integer duracao_minutos
) {
    public ServicoResponseDTO(Servico servico) {
        this(servico.getId(), servico.getNome(), servico.getDescricao(), servico.getPreco(), servico.getDuracao_minutos());
    }
}
