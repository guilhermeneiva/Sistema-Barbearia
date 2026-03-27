package com.guilhermeneiva.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RegisterAdminRequest(
        @NotBlank
        String email,
        @NotBlank
        String senha) {
}
