package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ServicoRequestDTO(
        @NotBlank(message = "O nome do serviço é obrigatório.")
        String nome,
        @NotBlank(message = "A descrição do serviço é obrigatória.")
        String descricao,
        @NotNull(message = "O preço não pode ser 0") @Positive(message = "O preço do serviço deve ser um valor positivo.")
        BigDecimal preco,
        @NotNull(message = "A duração não pode ser 0") @Min(10)
        Integer duracao_minutos
) {
}
