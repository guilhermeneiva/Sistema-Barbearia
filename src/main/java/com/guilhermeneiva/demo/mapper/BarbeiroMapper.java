package com.guilhermeneiva.demo.mapper;

import com.guilhermeneiva.demo.dto.request.BarbeiroRequestDTO;
import com.guilhermeneiva.demo.dto.response.BarbeiroResponseDTO;
import com.guilhermeneiva.demo.model.entity.Barbeiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BarbeiroMapper {

    BarbeiroResponseDTO toResponseDTO(Barbeiro barbeiro);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Barbeiro toEntity(BarbeiroRequestDTO barbeiroRequestDTO);

    default Page<BarbeiroResponseDTO> toPage(Page<Barbeiro> barbeiros) {
        return barbeiros.map(this::toResponseDTO);
    }
}