package com.guilhermeneiva.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "barbeiros")
public class Barbeiro extends Usuario {

    private String nome;

    private String CPF;

    private String telefone;

    private String especialidade;

}