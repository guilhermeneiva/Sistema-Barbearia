package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmail(String email);

    boolean existsByCPF(String CPF);

    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<Cliente> findByCPF(String cpf);


}