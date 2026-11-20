package com.mediturnos.main.dto;

public record RegisterRequest(
        String nombre,
        String apellido,
        String dni,
        String telefono,
        String contactoEmergencia,
        String email,
        String password
) {
}
