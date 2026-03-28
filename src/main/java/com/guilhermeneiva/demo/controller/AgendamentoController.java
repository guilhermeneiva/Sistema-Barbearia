package com.guilhermeneiva.demo.controller;

import com.guilhermeneiva.demo.config.JWTUserData;
import com.guilhermeneiva.demo.dto.request.AgendamentoRequestDTO;
import com.guilhermeneiva.demo.dto.response.AgendamentoResponseDTO;
import com.guilhermeneiva.demo.model.service.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    AgendamentoService agendamentoService;

    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> create(@RequestBody @Valid AgendamentoRequestDTO agendamentoRequestDTO) {
        AgendamentoResponseDTO agendamentoResponseDTO = agendamentoService.create(agendamentoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoResponseDTO);
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDTO>> findAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Page<AgendamentoResponseDTO> agendamentoResponseDTOS = agendamentoService.findAll(page, size, orderBy, direction);
        return ResponseEntity.ok(agendamentoResponseDTOS);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<Page<AgendamentoResponseDTO>> findByBarbeiro(
            @PathVariable Long barbeiroId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "dataInicio") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Page<AgendamentoResponseDTO> agendamentoResponseDTOS = agendamentoService.findByBarbeiro(barbeiroId, page, size, orderBy, direction);
        return ResponseEntity.ok(agendamentoResponseDTOS);
    }

    @GetMapping("/barbeiro/{barbeiroId}/data")
    public ResponseEntity<Page<AgendamentoResponseDTO>> findByBarbeiroAndData(
            @PathVariable Long barbeiroId,
            @RequestParam LocalDate data,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "dataInicio") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Page<AgendamentoResponseDTO> agendamentoResponseDTOS = agendamentoService.findByBarbeiroAndData(barbeiroId, data, page, size, orderBy, direction);
        return ResponseEntity.ok(agendamentoResponseDTOS);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id) {
        agendamentoService.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meus-agendamentos")
    public ResponseEntity<Page<AgendamentoResponseDTO>> findMeusAgendamentos(
            @AuthenticationPrincipal JWTUserData loggedUser,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "dataInicio") String orderBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        Page<AgendamentoResponseDTO> agendamentos = agendamentoService.findByCliente(loggedUser.userId(), page, size, orderBy, direction);
        return ResponseEntity.ok(agendamentos);
    }
}