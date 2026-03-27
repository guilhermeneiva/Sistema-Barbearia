package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {


    @Query("SELECT COUNT(a) > 0 FROM Agendamento a " +
            "WHERE a.barbeiro.id = :barbeiroId " +
            "AND (:inicio < a.dataFim AND :fim > a.dataInicio)")
    boolean existeConflito(@Param("barbeiroId") Long barbeiroId,
                           @Param("inicio") LocalDateTime inicio,
                           @Param("fim") LocalDateTime fim);


    @Query("SELECT a FROM Agendamento a WHERE a.barbeiro.id = :barbeiroId")
    Page<Agendamento> findByBarbeiro(Long barbeiroId, PageRequest pageRequest);

    Page<Agendamento> findByBarbeiroIdAndDataInicioBetween(Long barbeiroId, LocalDateTime data_inicio, LocalDateTime fim_data, PageRequest pageRequest);
}