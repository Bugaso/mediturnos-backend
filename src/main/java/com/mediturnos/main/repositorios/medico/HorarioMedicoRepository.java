package com.mediturnos.main.repositorios.medico;

import com.mediturnos.main.data.enums.DiaSemana;
import com.mediturnos.main.data.medico.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {
    List<HorarioMedico> findByMedico_IdMedico(Integer idMedico);
    List<HorarioMedico> findByMedico_IdMedicoAndDiaSemana(Integer idMedico, DiaSemana diaSemana);
}