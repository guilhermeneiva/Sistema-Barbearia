package com.guilhermeneiva.demo.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "admins")
public class Admin extends Usuario {

    public Admin(){
        super();
    }
}
