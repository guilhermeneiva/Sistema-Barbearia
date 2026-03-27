package com.guilhermeneiva.demo.model.service;

import com.guilhermeneiva.demo.dto.request.BarbeiroRequestDTO;
import com.guilhermeneiva.demo.dto.response.BarbeiroResponseDTO;
import com.guilhermeneiva.demo.exception.BarbeiroNaoEncontradoException;
import com.guilhermeneiva.demo.exception.CpfJaCadastradoException;
import com.guilhermeneiva.demo.mapper.BarbeiroMapper;
import com.guilhermeneiva.demo.model.entity.Barbeiro;
import com.guilhermeneiva.demo.model.enums.UserRole;
import com.guilhermeneiva.demo.model.repository.BarbeiroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BarbeiroService {

    private final BarbeiroRepository barbeiroRepository;
    private final BarbeiroMapper barbeiroMapper;
    private final PasswordEncoder passwordEncoder;

    public BarbeiroService(BarbeiroRepository barbeiroRepository, BarbeiroMapper barbeiroMapper, PasswordEncoder passwordEncoder) {
        this.barbeiroRepository = barbeiroRepository;
        this.barbeiroMapper = barbeiroMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public BarbeiroResponseDTO create(BarbeiroRequestDTO barbeiroRequestDTO) {
        if (barbeiroRepository.existsByCPF(barbeiroRequestDTO.CPF())) {
            throw new CpfJaCadastradoException("CPF já cadastrado!");
        }
        Barbeiro barbeiro = barbeiroMapper.toEntity(barbeiroRequestDTO);
        barbeiro.setSenha(passwordEncoder.encode(barbeiroRequestDTO.senha()));
        barbeiro.setRole(UserRole.BARBEIRO);
        Barbeiro barbeiro_salvo = barbeiroRepository.save(barbeiro);
        return barbeiroMapper.toResponseDTO(barbeiro_salvo);
    }

    public void delete(Long id) {
       Barbeiro barbeiro = barbeiroRepository.findById(id)
               .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro com o ID " + id + " não encontrado!"));
         barbeiroRepository.delete(barbeiro);
    }

    public Page<BarbeiroResponseDTO> findAll(Integer page, Integer size, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Barbeiro> barbeiros = barbeiroRepository.findAll(pageRequest);
        return barbeiroMapper.toPage(barbeiros);
    }

    public BarbeiroResponseDTO findById(Long id){
        return barbeiroRepository.findById(id).map(x -> barbeiroMapper.toResponseDTO(x))
                .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro com o ID " + id + " não encontrado!"));
    }

    public BarbeiroResponseDTO findByCPF(String cpf) {
        return barbeiroRepository.findByCPF(cpf).map(x -> barbeiroMapper.toResponseDTO(x))
                .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro com o CPF " + cpf + " não encontrado!"));
    }

    public BarbeiroResponseDTO findByName(String nome) {
        return barbeiroRepository.findByNome(nome).map(x -> barbeiroMapper.toResponseDTO(x))
                .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro com o nome " + nome + " não encontrado!"));
    }

    public BarbeiroResponseDTO update(Long id, BarbeiroRequestDTO barbeiroRequestDTO) {
        Barbeiro barbeiro = barbeiroRepository.findById(id)
                .orElseThrow(() -> new BarbeiroNaoEncontradoException("Barbeiro com o ID " + id + " não encontrado!"));

        if (!barbeiro.getCPF().equals(barbeiroRequestDTO.CPF())) {
            if (barbeiroRepository.existsByCPF(barbeiroRequestDTO.CPF())) {
                throw new CpfJaCadastradoException("CPF já cadastrado!");
            }
        }

        barbeiro.setNome(barbeiroRequestDTO.nome());
        barbeiro.setTelefone(barbeiroRequestDTO.telefone());
        barbeiro.setCPF(barbeiroRequestDTO.CPF());
        barbeiro.setEspecialidade(barbeiroRequestDTO.especialidade());

        return barbeiroMapper.toResponseDTO(barbeiroRepository.save(barbeiro));
    }
}