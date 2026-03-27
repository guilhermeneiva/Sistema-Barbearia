package com.guilhermeneiva.demo.controller;

import com.guilhermeneiva.demo.dto.request.ServicoRequestDTO;
import com.guilhermeneiva.demo.dto.response.ServicoResponseDTO;
import com.guilhermeneiva.demo.model.service.ServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> create(@RequestBody @Valid ServicoRequestDTO servicoRequestDTO) {
        ServicoResponseDTO servicoResponseDTO = servicoService.create(servicoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicoResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<ServicoResponseDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                            @RequestParam(value = "size", defaultValue = "3") Integer size) {
        Page<ServicoResponseDTO> servicoResponseDTO = servicoService.findAll(page, size);
        return ResponseEntity.ok(servicoResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> update(@PathVariable Long id, @Valid @RequestBody ServicoRequestDTO servicoRequestDTO) {
        ServicoResponseDTO servicoResponseDTO = servicoService.update(id, servicoRequestDTO);
        return ResponseEntity.ok(servicoResponseDTO);
    }
}