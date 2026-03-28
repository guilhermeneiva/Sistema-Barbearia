package com.guilhermeneiva.demo.controller;

import com.guilhermeneiva.demo.dto.request.ClienteRequestDTO;
import com.guilhermeneiva.demo.dto.response.ClienteResponseDTO;
import com.guilhermeneiva.demo.model.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteResponseDTO = clienteService.create(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "3") Integer size,
                                                            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ClienteResponseDTO> clienteResponseDTOS = clienteService.findAll(page, size, orderBy, direction);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDTOS);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> findByName(@RequestParam(value = "nome") String nome,
                                                               @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                               @RequestParam(value = "size", defaultValue = "3") Integer size,
                                                               @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                               @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<ClienteResponseDTO> clienteResponseDTOS = clienteService.findByName(nome, page, size, orderBy, direction);
        return ResponseEntity.ok(clienteResponseDTOS);
    }

    @GetMapping("/buscarPorCPF")
    public ResponseEntity<ClienteResponseDTO> findByCPF(@RequestParam String cpf) {
        ClienteResponseDTO clienteResponseDTO = clienteService.findByCPF(cpf);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> findByID(@PathVariable Long id) {
        ClienteResponseDTO clienteResponseDTO = clienteService.findById(id);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.userId")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable @Valid Long id, @RequestBody ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteResponseDTO = clienteService.update(id, clienteRequestDTO);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}