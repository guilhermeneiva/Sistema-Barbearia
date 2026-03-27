package com.guilhermeneiva.demo.mapper;

import com.guilhermeneiva.demo.dto.request.AgendamentoRequestDTO;
import com.guilhermeneiva.demo.dto.response.AgendamentoResponseDTO;
import com.guilhermeneiva.demo.model.entity.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AgendamentoMapper {

    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "nomeBarbeiro", source = "barbeiro.nome")
    @Mapping(target = "nomeServico", source = "servico.nome")
    AgendamentoResponseDTO toResponseDTO(Agendamento agendamento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "barbeiro", ignore = true)
    @Mapping(target = "servico", ignore = true)
    @Mapping(target = "statusAgendamento", ignore = true)
    @Mapping(target = "dataFim", ignore = true)
    Agendamento toEntity(AgendamentoRequestDTO agendamentoRequestDTO);

    default Page<AgendamentoResponseDTO> toPage(Page<Agendamento> agendamentos) {
        return agendamentos.map(this::toResponseDTO);
    }
}