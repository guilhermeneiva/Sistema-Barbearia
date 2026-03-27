package com.guilhermeneiva.demo.mapper;

import com.guilhermeneiva.demo.dto.request.ServicoRequestDTO;
import com.guilhermeneiva.demo.dto.response.ServicoResponseDTO;
import com.guilhermeneiva.demo.model.entity.Servico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ServicoMapper {

    ServicoResponseDTO toResponseDTO(Servico servico);

    @Mapping(target = "id", ignore = true)
    Servico toEntity(ServicoRequestDTO servicoRequestDTO);

    default Page<ServicoResponseDTO> toPage(Page<Servico> servicos){
     return servicos.map(this::toResponseDTO);
    }
}
