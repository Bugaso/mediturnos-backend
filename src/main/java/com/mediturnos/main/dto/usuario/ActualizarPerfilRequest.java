package com.mediturnos.main.dto.usuario;

import lombok.Data;

@Data
public class ActualizarPerfilRequest {
    private String nombre;
    private String apellido;
    private String telefono;
    private String contactoEmergencia;
}
