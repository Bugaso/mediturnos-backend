package com.mediturnos.main.servicios.medico;

import com.mediturnos.main.data.medico.Especialidad;
import com.mediturnos.main.repositorios.medico.EspecialidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EspecialidadService {

    private final EspecialidadRepository especialidadRepository;

    public List<Especialidad> listarActivas() {
        return especialidadRepository.findByEstadoTrue();
    }

    public List<Especialidad> listarTodas() {
        return especialidadRepository.findAll();
    }

    public Especialidad obtenerPorId(Integer idEspecialidad) {
        return especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + idEspecialidad));
    }
}
