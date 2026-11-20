package com.mediturnos.main.dto.usuario;

import lombok.Builder;

@Builder
public record AuthResponse(
        String token,
        String email,
        String nombre,
        String apellido,
        Integer idUsuario
) {
}
