package com.guilhermeneiva.demo.dto.response;

public record AgendamentoResponseDTO(
        Long id,
        String nomeCliente,
        String nomeBarbeiro,
        String nomeServico,
        String dataInicio,
        String statusAgendamento
) {
}