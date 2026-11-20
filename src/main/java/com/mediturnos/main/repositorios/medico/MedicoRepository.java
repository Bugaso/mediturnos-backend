package com.mediturnos.main.repositorios.medico;

import com.mediturnos.main.data.medico.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    List<Medico> findByMedicoActivoTrue();
    Optional<Medico> findByUsuario_IdUsuario(Integer idUsuario);
    Optional<Medico> findByMatricula(String matricula);
    List<Medico> findByEspecialidades_IdEspecialidadAndMedicoActivoTrue(Integer idEspecialidad);
}
