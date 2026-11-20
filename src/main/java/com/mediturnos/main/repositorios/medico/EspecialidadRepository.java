package com.mediturnos.main.repositorios.medico;

import com.mediturnos.main.data.medico.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {
    List<Especialidad> findByEstadoTrue();
    Optional<Especialidad> findByNombreEspecialidad(String nombreEspecialidad);
}
