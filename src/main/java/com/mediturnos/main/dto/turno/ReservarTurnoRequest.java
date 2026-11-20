package com.mediturnos.main.dto.turno;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ReservarTurnoRequest {
    private Integer idMedico;
    private Integer idEspecialidad;
    private LocalDate fecha;       // "2026-11-15"
    private LocalTime horaInicio;  // "09:30:00"
    private String motivoConsulta;

    // Requeridos únicamente si el usuario reserva como invitado (sin token JWT)
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
}
