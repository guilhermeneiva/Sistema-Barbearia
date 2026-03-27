package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(

        @NotNull(message = "ID do barbeiro não pode ser vazio")
        Long barbeiroId,
        @NotNull(message = "ID do cliente não pode ser vazio")
        Long clienteId,
        @NotNull(message = "ID do serviço não pode ser vazio")
        Long servicoId,
        @NotNull
        @Future
        LocalDateTime dataInicio
) {
}