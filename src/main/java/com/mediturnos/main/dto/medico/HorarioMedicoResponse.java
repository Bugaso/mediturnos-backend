package com.mediturnos.main.dto.medico;

import com.mediturnos.main.data.enums.DiaSemana;
import com.mediturnos.main.data.medico.HorarioMedico;
import lombok.Builder;
import lombok.Data;

import java.time.LocalTime;

@Data
@Builder
public class HorarioMedicoResponse {
    private Integer idHorario;
    private Integer idMedico;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public static HorarioMedicoResponse fromEntity(HorarioMedico h) {
        return HorarioMedicoResponse.builder()
                .idHorario(h.getIdHorario())
                .idMedico(h.getMedico().getIdMedico())
                .diaSemana(h.getDiaSemana())
                .horaInicio(h.getHoraInicio())
                .horaFin(h.getHoraFin())
                .build();
    }
}
