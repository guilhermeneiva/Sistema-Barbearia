package com.guilhermeneiva.demo.model.service;

import com.guilhermeneiva.demo.dto.request.AgendamentoRequestDTO;
import com.guilhermeneiva.demo.dto.response.AgendamentoResponseDTO;
import com.guilhermeneiva.demo.exception.*;
import com.guilhermeneiva.demo.mapper.AgendamentoMapper;
import com.guilhermeneiva.demo.model.entity.Agendamento;
import com.guilhermeneiva.demo.model.entity.Barbeiro;
import com.guilhermeneiva.demo.model.entity.Cliente;
import com.guilhermeneiva.demo.model.entity.Servico;
import com.guilhermeneiva.demo.model.enums.StatusAgendamento;
import com.guilhermeneiva.demo.model.repository.AgendamentoRepository;
import com.guilhermeneiva.demo.model.repository.BarbeiroRepository;
import com.guilhermeneiva.demo.model.repository.ClienteRepository;
import com.guilhermeneiva.demo.model.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class AgendamentoService {

    AgendamentoRepository agendamentoRepository;
    AgendamentoMapper agendamentoMapper;
    BarbeiroRepository barbeiroRepository;
    ServicoRepository servicoRepository;
    ClienteRepository clienteRepository;

    public AgendamentoService(AgendamentoRepository agendamentoRepository, AgendamentoMapper agendamentoMapper, BarbeiroRepository barbeiroRepository, ServicoRepository servicoRepository, ClienteRepository clienteRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoMapper = agendamentoMapper;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
        this.clienteRepository = clienteRepository;
    }

    public AgendamentoResponseDTO create(AgendamentoRequestDTO agendamentoRequestDTO) {

        validarDiaDeFuncionamento(agendamentoRequestDTO.dataInicio());
        validarHorarioDeFuncionamento(agendamentoRequestDTO.dataInicio());

        Servico servico = servicoRepository.findById(agendamentoRequestDTO.servicoId())
                .orElseThrow(() -> new ServicoNaoEncontradoException("Serviço não encontrado"));

        Barbeiro barbeiro = barbeiroRepository.findById(agendamentoRequestDTO.barbeiroId())
                .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro não encontrado"));

        Cliente cliente = clienteRepository.findById(agendamentoRequestDTO.clienteId())
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));

        LocalDateTime fim = agendamentoRequestDTO.dataInicio().plusMinutes(servico.getDuracao_minutos());

        if (agendamentoRepository.existeConflito(barbeiro.getId(), agendamentoRequestDTO.dataInicio(), fim)) {
            throw new HorarioComConflitoException("O barbeiro já possui um agendamento nesse horário");
        }

        Agendamento agendamento = agendamentoMapper.toEntity(agendamentoRequestDTO);

        agendamento.setCliente(cliente);
        agendamento.setBarbeiro(barbeiro);
        agendamento.setServico(servico);
        agendamento.setDataFim(fim);
        agendamento.setStatusAgendamento(StatusAgendamento.AGENDADO);

        Agendamento agendamentoSalvo = agendamentoRepository.save(agendamento);
        return agendamentoMapper.toResponseDTO(agendamentoSalvo);
    }

    public Page<AgendamentoResponseDTO> findAll(Integer page, Integer size, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Agendamento> agendamentos = agendamentoRepository.findAll(pageRequest);
        return agendamentoMapper.toPage(agendamentos);
    }

    public Page<AgendamentoResponseDTO> findByBarbeiro(Long barbeiroId, Integer page, Integer size, String orderBy, String direction) {
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new BarbeiroNaoEncontradoException("Barbeiro com o ID " + barbeiroId + " não encontrado!");
        }
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Agendamento> agendamentos = agendamentoRepository.findByBarbeiro(barbeiroId, pageRequest);
        return agendamentoMapper.toPage(agendamentos);
    }

    public Page<AgendamentoResponseDTO> findByBarbeiroAndData(Long barbeiroId, LocalDate data, Integer page, Integer size, String orderBy, String direction) {
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new BarbeiroNaoEncontradoException("Barbeiro com o ID " + barbeiroId + " não encontrado!");
        }
        LocalDateTime inicio_data = data.atStartOfDay();
        LocalDateTime fim_data = data.atTime(LocalTime.MAX);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Agendamento> agendamentos = agendamentoRepository.findByBarbeiroIdAndDataInicioBetween(barbeiroId, inicio_data, fim_data, pageRequest);
        if (agendamentos.isEmpty()) {
            throw new AgendamentoNaoEncontradoException("Nenhum agendamento encontrado para o barbeiro com o ID " + barbeiroId + " na data " + data);
        }
        return agendamentoMapper.toPage(agendamentos);
    }

    public void validarDiaDeFuncionamento(LocalDateTime data) {
        DayOfWeek diaDaSemana = data.getDayOfWeek();
        if (diaDaSemana == DayOfWeek.SUNDAY) {
            throw new DataDeFuncionamentoException("A barbearia não funciona aos domingos");
        }
    }

    public void validarHorarioDeFuncionamento(LocalDateTime data) {
        if (data.isBefore(data.toLocalDate().atTime(8, 0)) || data.isAfter(data.toLocalDate().atTime(17, 30))) {
            throw new HorarioDeFuncionamentoException("A barbearia funciona das 8h às 18h");
        }
    }

    public void cancelarAgendamento(Long id) {
        Agendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new AgendamentoNaoEncontradoException("Agendamento com o ID " + id + " não encontrado!"));

        if (agendamento.getStatusAgendamento() == StatusAgendamento.CANCELADO) {
            throw new AgendamentoJaCanceladoException("Agendamento com o ID " + id + " já está cancelado!");
        }

        validarRegrasDeTempoParaCancelamento(agendamento.getDataInicio());

        agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
    }

    public void validarRegrasDeTempoParaCancelamento(LocalDateTime dataInicio) {
        LocalDateTime agora = LocalDateTime.now();
        if (dataInicio.isBefore(agora)) {
            throw new IllegalStateException("Não é possível cancelar um agendamento que já ocorreu!");
        }

        if (Duration.between(agora, dataInicio).toMinutes() < 120) {
            throw new HorasParaAgendamentoException("O cancelamento deve ser feito com pelo menos 2 horas de antecedência!");
        }
    }
}