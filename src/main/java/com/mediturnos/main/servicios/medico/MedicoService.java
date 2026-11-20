package com.mediturnos.main.servicios.medico;

import com.mediturnos.main.data.enums.DiaSemana;
import com.mediturnos.main.data.medico.HorarioMedico;
import com.mediturnos.main.data.medico.Medico;

import com.mediturnos.main.repositorios.medico.HorarioMedicoRepository;
import com.mediturnos.main.repositorios.medico.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;

    public List<Medico> listarActivos() {
        return medicoRepository.findByMedicoActivoTrue();
    }

    public List<Medico> listarPorEspecialidad(Integer idEspecialidad) {
        return medicoRepository.findByEspecialidades_IdEspecialidadAndMedicoActivoTrue(idEspecialidad);
    }

    public Medico obtenerPorId(Integer idMedico) {
        return medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado con ID: " + idMedico));
    }

    public Medico obtenerPorUsuario(Integer idUsuario) {
        return medicoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene un perfil médico asociado"));
    }

    public List<HorarioMedico> obtenerHorarios(Integer idMedico) {
        return horarioMedicoRepository.findByMedico_IdMedico(idMedico);
    }

    public List<HorarioMedico> obtenerHorariosPorDia(Integer idMedico, DiaSemana diaSemana) {
        return horarioMedicoRepository.findByMedico_IdMedicoAndDiaSemana(idMedico, diaSemana);
    }
}
