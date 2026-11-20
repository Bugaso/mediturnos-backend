package com.mediturnos.main.repositorios.turno;

import com.mediturnos.main.data.enums.EstadoTurno;
import com.mediturnos.main.data.turno.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Integer> {
    List<Turno> findByUsuario_IdUsuarioOrderByFechaDescHoraInicioDesc(Integer idUsuario);
    List<Turno> findByUsuario_IdUsuarioAndEstado(Integer idUsuario, EstadoTurno estado);
    List<Turno> findByMedico_IdMedicoAndFecha(Integer idMedico, LocalDate fecha);
    List<Turno> findByMedico_IdMedicoAndFechaAndEstadoNot(Integer idMedico, LocalDate fecha, EstadoTurno estado);
    boolean existsByMedico_IdMedicoAndFechaAndHoraInicioAndEstadoNot(
            Integer idMedico,
            LocalDate fecha,
            LocalTime horaInicio,
            EstadoTurno estado
    );
}
