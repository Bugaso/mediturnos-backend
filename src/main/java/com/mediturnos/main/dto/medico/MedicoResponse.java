package com.mediturnos.main.dto.medico;

import com.mediturnos.main.data.medico.Medico;
import com.mediturnos.main.dto.especialidad.EspecialidadResponse;
import com.mediturnos.main.dto.obrasocial.ObraSocialResponse;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@Builder
public class MedicoResponse {
    private Integer idMedico;
    private String nombre;
    private String apellido;
    private String matricula;
    private Boolean activo;
    private List<EspecialidadResponse> especialidades;
    private List<HorarioMedicoResponse> horarios;
    private List<ObraSocialResponse> obrasSociales; // <-- Nuevo campo

    public static MedicoResponse fromEntity(Medico m) {
        // Obtenemos las obras sociales asociadas al usuario del médico
        List<ObraSocialResponse> listaObrasSociales = (m.getUsuario() != null && m.getUsuario().getObrasSociales() != null)
                ? m.getUsuario().getObrasSociales().stream()
                // Si getObrasSociales() devuelve ObraSocialUsuario, usa: .map(ou -> ObraSocialResponse.fromEntity(ou.getObraSocial()))
                // Si devuelve directamente ObraSocial, usa: .map(ObraSocialResponse::fromEntity)
                .map(ou -> ObraSocialResponse.fromEntity(ou.getObraSocial()))
                .toList()
                : Collections.emptyList();

        return MedicoResponse.builder()
                .idMedico(m.getIdMedico())
                .nombre(m.getUsuario().getNombre())
                .apellido(m.getUsuario().getApellido())
                .matricula(m.getMatricula())
                .activo(m.getMedicoActivo())
                .especialidades(
                        m.getEspecialidades() != null
                                ? m.getEspecialidades().stream()
                                .map(EspecialidadResponse::fromEntity)
                                .toList()
                                : Collections.emptyList()
                )
                .horarios(
                        m.getHorarios() != null
                                ? m.getHorarios().stream()
                                .map(HorarioMedicoResponse::fromEntity)
                                .toList()
                                : Collections.emptyList()
                )
                .obrasSociales(listaObrasSociales)
                .build();
    }
}