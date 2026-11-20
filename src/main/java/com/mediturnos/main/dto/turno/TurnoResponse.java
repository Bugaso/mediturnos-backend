package com.mediturnos.main.dto.turno;

import com.mediturnos.main.data.enums.EstadoTurno;
import com.mediturnos.main.data.turno.Turno;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TurnoResponse {
    private Integer idTurno;
    private Integer idUsuario;
    private String nombrePaciente;
    private Integer idMedico;
    private String nombreMedico;
    private Integer idEspecialidad;
    private String nombreEspecialidad;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private String motivoConsulta;
    private EstadoTurno estado;

    public static TurnoResponse fromEntity(Turno t) {
        return TurnoResponse.builder()
                .idTurno(t.getIdTurno())
                .idUsuario(t.getUsuario().getIdUsuario())
                .nombrePaciente(t.getUsuario().getNombre() + " " + t.getUsuario().getApellido())
                .idMedico(t.getMedico().getIdMedico())
                .nombreMedico(t.getMedico().getUsuario().getNombre() + " " + t.getMedico().getUsuario().getApellido())
                .idEspecialidad(t.getEspecialidad().getIdEspecialidad())
                .nombreEspecialidad(t.getEspecialidad().getNombreEspecialidad())
                .fecha(t.getFecha())
                .horaInicio(t.getHoraInicio())
                .motivoConsulta(t.getMotivoConsulta())
                .estado(t.getEstado())
                .build();
    }
}
