package com.guilhermeneiva.demo;

import com.guilhermeneiva.demo.model.entity.Admin;
import com.guilhermeneiva.demo.model.enums.UserRole;
import com.guilhermeneiva.demo.model.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ProjetoBarbeariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoBarbeariaApplication.class, args);
    }


    @Bean
    public CommandLineRunner seeAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder, @Value("${admin.email}") String adminEmail, @Value("${admin.senha}") String adminSenha) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setEmail(adminEmail);
                admin.setSenha(passwordEncoder.encode(adminSenha));
                admin.setRole(UserRole.ADMIN);
                adminRepository.save(admin);
            }
        };
    }
}