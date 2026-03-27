package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Barbeiro;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BarbeiroRepository extends JpaRepository<Barbeiro, Long> {

    boolean existsByCPF(@NotBlank(message = "CPF já cadastrado!") String cpf);

    Optional<Barbeiro> findByCPF(String cpf);

    Optional<Barbeiro> findByNome(String nome);

}
