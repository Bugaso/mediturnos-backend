package com.mediturnos.main.servicios.turno;

import com.mediturnos.main.data.enums.DiaSemana;
import com.mediturnos.main.data.enums.EstadoTurno;
import com.mediturnos.main.data.medico.Especialidad;
import com.mediturnos.main.data.medico.HorarioMedico;
import com.mediturnos.main.data.medico.Medico;
import com.mediturnos.main.data.turno.Turno;
import com.mediturnos.main.data.usuario.Usuario;
import com.mediturnos.main.dto.turno.ReservarTurnoRequest;
import com.mediturnos.main.repositorios.medico.EspecialidadRepository;
import com.mediturnos.main.repositorios.medico.HorarioMedicoRepository;
import com.mediturnos.main.repositorios.medico.MedicoRepository;
import com.mediturnos.main.repositorios.turno.TurnoRepository;
import com.mediturnos.main.repositorios.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TurnoService {

    private final TurnoRepository turnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MedicoRepository medicoRepository;
    private final EspecialidadRepository especialidadRepository;
    private final HorarioMedicoRepository horarioMedicoRepository;

    public List<Turno> listarPorUsuario(Integer idUsuario) {
        return turnoRepository.findByUsuario_IdUsuarioOrderByFechaDescHoraInicioDesc(idUsuario);
    }

    public List<Turno> listarActivosPorUsuario(Integer idUsuario) {
        return turnoRepository.findByUsuario_IdUsuarioAndEstado(idUsuario, EstadoTurno.activo);
    }

    public List<Turno> listarAgendaMedicoPorFecha(Integer idMedico, LocalDate fecha) {
        return turnoRepository.findByMedico_IdMedicoAndFechaAndEstadoNot(
                idMedico,
                fecha,
                EstadoTurno.cancelado
        );
    }

    public List<LocalTime> obtenerHorariosDisponibles(Integer idMedico, LocalDate fecha, int duracionMinutos) {
        DiaSemana diaSemana = convertirDiaSemana(fecha.getDayOfWeek());
        List<HorarioMedico> franjas = horarioMedicoRepository.findByMedico_IdMedicoAndDiaSemana(idMedico, diaSemana);



        if (franjas.isEmpty()) {
            return List.of();
        }

        Set<LocalTime> horasOcupadas = turnoRepository
                .findByMedico_IdMedicoAndFechaAndEstadoNot(idMedico, fecha, EstadoTurno.cancelado)
                .stream()
                .map(Turno::getHoraInicio)
                .collect(Collectors.toSet());

        List<LocalTime> disponibles = new ArrayList<>();
        for (HorarioMedico franja : franjas) {
            LocalTime cursor = franja.getHoraInicio();
            while (!cursor.plusMinutes(duracionMinutos).isAfter(franja.getHoraFin())) {
                if (!horasOcupadas.contains(cursor)) {
                    disponibles.add(cursor);
                }
                cursor = cursor.plusMinutes(duracionMinutos);
            }
        }



        return disponibles.stream().filter(disponible -> {
            return LocalDate.now().isBefore(fecha) || LocalTime.now().isBefore(disponible);
        }).toList();
    }

    @Transactional
    public Turno reservarTurnoAutenticado(String emailUsuario, ReservarTurnoRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));

        return crearYGuardarTurno(
                usuario,
                request.getIdMedico(),
                request.getIdEspecialidad(),
                request.getFecha(),
                request.getHoraInicio(),
                request.getMotivoConsulta()
        );
    }

    @Transactional
    public Turno reservarTurnoInvitado(ReservarTurnoRequest request) {
        if (isBlank(request.getNombre()) || isBlank(request.getApellido())
                || isBlank(request.getDni()) || isBlank(request.getTelefono())) {
            throw new RuntimeException("Nombre, apellido, DNI y teléfono son obligatorios para reservar sin cuenta");
        }

        String dniLimpio = request.getDni().trim();

        // Buscamos si ya existe un registro con ese DNI (recordando que dni es UNIQUE en la BD)
        Usuario usuario = usuarioRepository.findByDni(dniLimpio)
                .map(existente -> {
                    if (existente.getEmail() != null || existente.getPassword() != null) {
                        throw new RuntimeException("El DNI ingresado pertenece a una cuenta registrada. Inicia sesión para reservar.");
                    }
                    // Actualizamos teléfono/datos si volvió a reservar como invitado
                    existente.setNombre(request.getNombre().trim());
                    existente.setApellido(request.getApellido().trim());
                    existente.setTelefono(request.getTelefono().trim());
                    return usuarioRepository.save(existente);
                })
                .orElseGet(() -> {
                    Usuario nuevoInvitado = Usuario.builder()
                            .nombre(request.getNombre().trim())
                            .apellido(request.getApellido().trim())
                            .dni(dniLimpio)
                            .telefono(request.getTelefono().trim())
                            .usuarioActivo(true)
                            .build();
                    return usuarioRepository.save(nuevoInvitado);
                });

        return crearYGuardarTurno(
                usuario,
                request.getIdMedico(),
                request.getIdEspecialidad(),
                request.getFecha(),
                request.getHoraInicio(),
                request.getMotivoConsulta()
        );
    }

    private Turno crearYGuardarTurno(
            Usuario usuario,
            Integer idMedico,
            Integer idEspecialidad,
            LocalDate fecha,
            LocalTime horaInicio,
            String motivoConsulta
    ) {
        if (Boolean.FALSE.equals(usuario.getUsuarioActivo())) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        Medico medico = medicoRepository.findById(idMedico)
                .orElseThrow(() -> new RuntimeException("Médico no encontrado"));

        if (Boolean.FALSE.equals(medico.getMedicoActivo())) {
            throw new RuntimeException("El médico seleccionado no se encuentra activo");
        }

        Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        boolean atiendeEspecialidad = medico.getEspecialidades().stream()
                .anyMatch(e -> e.getIdEspecialidad().equals(idEspecialidad));

        if (!atiendeEspecialidad) {
            throw new RuntimeException("El médico no atiende la especialidad seleccionada");
        }

        boolean horarioOcupado = turnoRepository.existsByMedico_IdMedicoAndFechaAndHoraInicioAndEstadoNot(
                idMedico,
                fecha,
                horaInicio,
                EstadoTurno.cancelado
        );

        if (horarioOcupado) {
            throw new RuntimeException("El horario seleccionado ya no se encuentra disponible");
        }

        Turno turno = Turno.builder()
                .usuario(usuario)
                .medico(medico)
                .especialidad(especialidad)
                .fecha(fecha)
                .horaInicio(horaInicio)
                .motivoConsulta(motivoConsulta)
                .estado(EstadoTurno.activo)
                .build();

        return turnoRepository.save(turno);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    @Transactional
    public Turno cancelarTurno(Integer idTurno, Integer idUsuario) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + idTurno));

        if (!turno.getUsuario().getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para cancelar este turno");
        }

        if (turno.getEstado() != EstadoTurno.activo) {
            throw new RuntimeException("Solo se pueden cancelar turnos en estado activo");
        }

        turno.setEstado(EstadoTurno.cancelado);
        return turnoRepository.save(turno);
    }

    @Transactional
    public Turno completarTurno(Integer idTurno) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + idTurno));

        turno.setEstado(EstadoTurno.completado);
        return turnoRepository.save(turno);
    }

    private DiaSemana convertirDiaSemana(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> DiaSemana.Lunes;
            case TUESDAY -> DiaSemana.Martes;
            case WEDNESDAY -> DiaSemana.Miercoles;
            case THURSDAY -> DiaSemana.Jueves;
            case FRIDAY -> DiaSemana.Viernes;
            case SATURDAY -> DiaSemana.Sabado;
            case SUNDAY -> DiaSemana.Domingo;
        };
    }

    @Transactional
    public Turno cancelarTurnoInvitado(Integer idTurno, Integer idUsuario) {
        Turno turno = turnoRepository.findById(idTurno)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con ID: " + idTurno));

        Usuario dueno = turno.getUsuario();
        if (!dueno.getIdUsuario().equals(idUsuario)) {
            throw new RuntimeException("No tienes permiso para cancelar este turno");
        }

        // Seguridad: solo permite cancelar sin JWT si el usuario es invitado (sin email ni password)
        if (dueno.getEmail() != null || dueno.getPassword() != null) {
            throw new RuntimeException("Este turno pertenece a una cuenta registrada. Inicia sesión para cancelarlo.");
        }

        if (turno.getEstado() != EstadoTurno.activo) {
            throw new RuntimeException("Solo se pueden cancelar turnos en estado activo");
        }

        turno.setEstado(EstadoTurno.cancelado);
        return turnoRepository.save(turno);
    }
}