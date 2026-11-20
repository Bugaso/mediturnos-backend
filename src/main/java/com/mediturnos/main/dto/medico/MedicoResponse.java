package com.mediturnos.main.dto.medico;

import com.mediturnos.main.data.medico.Medico;
import com.mediturnos.main.dto.especialidad.EspecialidadResponse;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
public class MedicoResponse {
    private Integer idMedico;
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String matricula;
    private Boolean medicoActivo;
    private List<EspecialidadResponse> especialidades;

    public static MedicoResponse fromEntity(Medico m) {
        return MedicoResponse.builder()
                .idMedico(m.getIdMedico())
                .idUsuario(m.getUsuario().getIdUsuario())
                .nombre(m.getUsuario().getNombre())
                .apellido(m.getUsuario().getApellido())
                .matricula(m.getMatricula())
                .medicoActivo(m.getMedicoActivo())
                .especialidades(m.getEspecialidades().stream()
                        .map(EspecialidadResponse::fromEntity)
                        .toList())
                .build();
    }
}
