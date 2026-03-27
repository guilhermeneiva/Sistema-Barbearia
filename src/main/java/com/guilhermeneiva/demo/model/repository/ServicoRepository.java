package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Servico;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {

    boolean existsByNome(@NotBlank(message = "O nome do serviço é obrigatório.") String nome);
}