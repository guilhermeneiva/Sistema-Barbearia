package com.guilhermeneiva.demo.controller;

import com.guilhermeneiva.demo.dto.request.BarbeiroRequestDTO;
import com.guilhermeneiva.demo.dto.response.BarbeiroResponseDTO;
import com.guilhermeneiva.demo.model.service.BarbeiroService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/barbeiros")
public class BarbeiroController {

    private final BarbeiroService barbeiroService;

    public BarbeiroController(BarbeiroService barbeiroService) {
        this.barbeiroService = barbeiroService;
    }

    @PostMapping
    public ResponseEntity<BarbeiroResponseDTO> create(@RequestBody @Valid BarbeiroRequestDTO barbeiroRequestDTO) {
        BarbeiroResponseDTO barbeiroResponseDTO = barbeiroService.create(barbeiroRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(barbeiroResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        barbeiroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<BarbeiroResponseDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "3") Integer size,
                                                             @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
                                                             @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<BarbeiroResponseDTO> clienteResponseDTO = barbeiroService.findAll(page, size, orderBy, direction);
        return ResponseEntity.status(HttpStatus.OK).body(clienteResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> findByID(@PathVariable Long id) {
        BarbeiroResponseDTO clienteResponseDTO = barbeiroService.findById(id);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @GetMapping("/buscarPorCPF")
    public ResponseEntity<BarbeiroResponseDTO> findByCPF(@RequestParam String cpf) {
        BarbeiroResponseDTO clienteResponseDTO = barbeiroService.findByCPF(cpf);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @GetMapping("/buscar")
    public ResponseEntity<BarbeiroResponseDTO> findByName(@RequestParam String nome) {
        BarbeiroResponseDTO clienteResponseDTO = barbeiroService.findByName(nome);
        return ResponseEntity.ok(clienteResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BarbeiroResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BarbeiroRequestDTO barbeiroRequestDTO) {
        BarbeiroResponseDTO barbeiroResponseDTO = barbeiroService.update(id, barbeiroRequestDTO);
        return ResponseEntity.ok(barbeiroResponseDTO);
    }
}