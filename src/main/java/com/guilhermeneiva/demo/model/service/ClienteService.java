package com.guilhermeneiva.demo.model.service;

import com.guilhermeneiva.demo.dto.request.ClienteRequestDTO;
import com.guilhermeneiva.demo.dto.request.RegisterClienteRequest;
import com.guilhermeneiva.demo.dto.response.ClienteResponseDTO;
import com.guilhermeneiva.demo.dto.response.RegisterClienteResponse;
import com.guilhermeneiva.demo.exception.ClienteNaoEncontradoException;
import com.guilhermeneiva.demo.exception.CpfJaCadastradoException;
import com.guilhermeneiva.demo.exception.EmailJaCadastradoException;
import com.guilhermeneiva.demo.mapper.ClienteMapper;
import com.guilhermeneiva.demo.model.entity.Cliente;
import com.guilhermeneiva.demo.model.enums.UserRole;
import com.guilhermeneiva.demo.model.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public ClienteResponseDTO create(ClienteRequestDTO clienteRequestDTO) {
        if (clienteRepository.existsByEmail(clienteRequestDTO.email())) {
            throw new EmailJaCadastradoException("Email já cadastrado!");
        }
        if (clienteRepository.existsByCPF(clienteRequestDTO.CPF())) {
            throw new CpfJaCadastradoException("CPF já cadastrado!");
        }
        Cliente cliente = clienteMapper.toEntity(clienteRequestDTO);
        cliente.setSenha(passwordEncoder.encode(clienteRequestDTO.senha()));
        cliente.setRole(UserRole.CLIENTE);
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteMapper.toResponseDTO(clienteSalvo);
    }

    public Page<ClienteResponseDTO> findAll(Integer page, Integer size, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Cliente> clientes = clienteRepository.findAll(pageRequest);
        return clienteMapper.toPage(clientes);
    }

    public Page<ClienteResponseDTO> findByName(String nome, Integer page, Integer size, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(direction), orderBy);
        Page<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome, pageRequest);
        return clienteMapper.toPage(clientes);
    }

    public ClienteResponseDTO findByCPF(String cpf) {
        return clienteRepository.findByCPF(cpf).map(x -> clienteMapper.toResponseDTO(x))
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente com o CPF " + cpf + " não encontrado!"));
    }

    public ClienteResponseDTO findById(Long id) {
        return clienteRepository.findById(id).map(x -> clienteMapper.toResponseDTO(x))
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente com o ID " + id + " não encontrado!"));
    }

    public ClienteResponseDTO update(Long id, ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente com o ID " + id + " não encontrado!"));

        if (!cliente.getEmail().equals(clienteRequestDTO.email())) {
            if (clienteRepository.existsByEmail(clienteRequestDTO.email())) {
                throw new EmailJaCadastradoException("Email já cadastrado!");
            }
        }

        if (!cliente.getCPF().equals(clienteRequestDTO.CPF())) {
            if (clienteRepository.existsByCPF(clienteRequestDTO.CPF())) {
                throw new CpfJaCadastradoException("CPF já cadastrado!");
            }
        }

        cliente.setNome(clienteRequestDTO.nome());
        cliente.setData_de_nascimento(clienteRequestDTO.data_de_nascimento());
        cliente.setEmail(clienteRequestDTO.email());
        cliente.setTelefone(clienteRequestDTO.telefone());
        cliente.setCPF(clienteRequestDTO.CPF());

        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    public void delete(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente com o ID " + id + " não encontrado!"));
        clienteRepository.deleteById(id);
    }

    public RegisterClienteResponse registerAuth(RegisterClienteRequest registerClienteRequest) {

        if (clienteRepository.existsByEmail(registerClienteRequest.email())) {
            throw new EmailJaCadastradoException("Email já cadastrado");
        }

        if (clienteRepository.existsByCPF(registerClienteRequest.CPF())) {
            throw new CpfJaCadastradoException("CPF já cadastrado");
        }

        Cliente cliente = clienteMapper.toEntityFromRegister(registerClienteRequest);
        cliente.setSenha(passwordEncoder.encode(registerClienteRequest.senha()));
        cliente.setRole(UserRole.CLIENTE);
        clienteRepository.save(cliente);

        return new RegisterClienteResponse(cliente.getNome(), cliente.getEmail());

    }

}