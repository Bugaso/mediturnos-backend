package com.mediturnos.main.dto.usuario;

public record LoginRequest(
        String email,
        String dni,
        String password
) {
}
