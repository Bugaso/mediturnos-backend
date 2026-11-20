package com.mediturnos.main.dto.especialidad;

import com.mediturnos.main.data.medico.Especialidad;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EspecialidadResponse {
    private Integer idEspecialidad;
    private String nombreEspecialidad;
    private Boolean estado;

    public static EspecialidadResponse fromEntity(Especialidad e) {
        return EspecialidadResponse.builder()
                .idEspecialidad(e.getIdEspecialidad())
                .nombreEspecialidad(e.getNombreEspecialidad())
                .estado(e.getEstado())
                .build();
    }
}
