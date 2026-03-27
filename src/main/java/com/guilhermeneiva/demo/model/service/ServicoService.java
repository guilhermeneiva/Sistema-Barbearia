package com.guilhermeneiva.demo.model.service;

import com.guilhermeneiva.demo.dto.request.ServicoRequestDTO;
import com.guilhermeneiva.demo.dto.response.ServicoResponseDTO;
import com.guilhermeneiva.demo.exception.NomeJaCadastradoException;
import com.guilhermeneiva.demo.exception.ServicoNaoEncontradoException;
import com.guilhermeneiva.demo.mapper.ServicoMapper;
import com.guilhermeneiva.demo.model.entity.Servico;
import com.guilhermeneiva.demo.model.repository.ServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository;
    private final ServicoMapper servicoMapper;

    public ServicoService(ServicoRepository servicoRepository, ServicoMapper servicoMapper) {
        this.servicoRepository = servicoRepository;
        this.servicoMapper = servicoMapper;
    }

    public ServicoResponseDTO create(ServicoRequestDTO servicoRequestDTO) {
       if (servicoRepository.existsByNome(servicoRequestDTO.nome())){
              throw new NomeJaCadastradoException("Já existe um serviço com esse nome!");
       }
        Servico servico = servicoMapper.toEntity(servicoRequestDTO);
        Servico servicoSalvo = servicoRepository.save(servico);
        return servicoMapper.toResponseDTO(servicoSalvo);
    }

    public Page<ServicoResponseDTO> findAll(Integer page, Integer size) {
        Page<Servico> servicos = servicoRepository.findAll(PageRequest.of(page, size));
        return servicoMapper.toPage(servicos);
    }

    public void deleteById(Long id) {
        Servico servico = servicoRepository.findById(id).
                orElseThrow(() -> new ServicoNaoEncontradoException("Serviço com o ID " + id + " não encontrado!"));
        servicoRepository.deleteById(id);
    }

    public ServicoResponseDTO update(Long id, ServicoRequestDTO servicoRequestDTO) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new ServicoNaoEncontradoException("Serviço com o ID " + id + " não encontrado!"));

        if (!servico.getNome().equals(servicoRequestDTO.nome())) {
            if (servicoRepository.existsByNome(servicoRequestDTO.nome())) {
                throw new NomeJaCadastradoException("Já existe um serviço com esse nome!");
            }
        }

        servico.setNome(servicoRequestDTO.nome());
        servico.setPreco(servicoRequestDTO.preco());
        servico.setDescricao(servicoRequestDTO.descricao());
        servico.setDuracao_minutos(servicoRequestDTO.duracao_minutos());

        return servicoMapper.toResponseDTO(servicoRepository.save(servico));
    }
}