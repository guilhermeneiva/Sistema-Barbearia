package com.guilhermeneiva.demo.mapper;

import com.guilhermeneiva.demo.dto.request.ClienteRequestDTO;
import com.guilhermeneiva.demo.dto.request.RegisterClienteRequest;
import com.guilhermeneiva.demo.dto.response.ClienteResponseDTO;
import com.guilhermeneiva.demo.model.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ClienteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target =  "role", ignore = true)
    Cliente toEntityFromRegister(RegisterClienteRequest registerClienteRequest);


    ClienteResponseDTO toResponseDTO(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "senha", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "role", ignore = true)
    Cliente toEntity(ClienteRequestDTO clienteRequestDTO);

    default Page<ClienteResponseDTO> toPage(Page<Cliente> clientes){
     return clientes.map(this::toResponseDTO);
    }
}