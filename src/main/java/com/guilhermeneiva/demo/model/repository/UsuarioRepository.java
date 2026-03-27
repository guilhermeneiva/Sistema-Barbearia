package com.guilhermeneiva.demo.model.repository;

import com.guilhermeneiva.demo.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findUserByEmail(String email);

    boolean existsByEmail(String email);
}
