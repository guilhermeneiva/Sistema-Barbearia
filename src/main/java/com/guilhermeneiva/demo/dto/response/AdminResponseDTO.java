package com.guilhermeneiva.demo.dto.response;

import com.guilhermeneiva.demo.model.entity.Admin;

public record AdminResponseDTO(
        Long id,
        String email
) {
    public AdminResponseDTO(Admin admin) {
        this(admin.getId(), admin.getEmail());
    }
}
