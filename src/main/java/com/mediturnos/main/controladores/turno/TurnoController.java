package com.mediturnos.main.controladores.turno;

import com.mediturnos.main.data.turno.Turno;
import com.mediturnos.main.dto.turno.ReservarTurnoRequest;
import com.mediturnos.main.dto.turno.TurnoResponse;
import com.mediturnos.main.servicios.turno.TurnoService;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TurnoResponse>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<TurnoResponse> turnos = turnoService.listarPorUsuario(idUsuario).stream()
                .map(TurnoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/usuario/{idUsuario}/activos")
    public ResponseEntity<List<TurnoResponse>> listarActivosPorUsuario(@PathVariable Integer idUsuario) {
        List<TurnoResponse> turnos = turnoService.listarActivosPorUsuario(idUsuario).stream()
                .map(TurnoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/medico/{idMedico}")
    public ResponseEntity<List<TurnoResponse>> listarAgendaMedicoPorFecha(
            @PathVariable Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        List<TurnoResponse> turnos = turnoService.listarAgendaMedicoPorFecha(idMedico, fecha).stream()
                .map(TurnoResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(turnos);
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<LocalTime>> obtenerHorariosDisponibles(
            @RequestParam Integer idMedico,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(defaultValue = "30") int duracionMinutos
    ) {
        return ResponseEntity.ok(
                turnoService.obtenerHorariosDisponibles(idMedico, fecha, duracionMinutos)
        );
    }

    @PostMapping
    public ResponseEntity<TurnoResponse> reservarTurno(
            @RequestBody ReservarTurnoRequest request,
            Authentication authentication
    ) {
        Turno turno;

        if (authentication != null && authentication.isAuthenticated()) {
            // Usuario logueado: sus datos se toman automáticamente desde su cuenta asociada al JWT
            String emailUsuario = authentication.getName();
            turno = turnoService.reservarTurnoAutenticado(emailUsuario, request);
        } else {
            // Usuario invitado: se valida y registra con nombre, apellido, dni y telefono
            turno = turnoService.reservarTurnoInvitado(request);
        }

        return ResponseEntity.ok(TurnoResponse.fromEntity(turno));
    }

    @PatchMapping("/{idTurno}/cancelar")
    public ResponseEntity<TurnoResponse> cancelarTurno(
            @PathVariable Integer idTurno,
            @RequestParam Integer idUsuario
    ) {
        var turno = turnoService.cancelarTurno(idTurno, idUsuario);
        return ResponseEntity.ok(TurnoResponse.fromEntity(turno));
    }

    @PatchMapping("/{idTurno}/completar")
    public ResponseEntity<TurnoResponse> completarTurno(@PathVariable Integer idTurno) {
        var turno = turnoService.completarTurno(idTurno);
        return ResponseEntity.ok(TurnoResponse.fromEntity(turno));
    }

    @PatchMapping("/{idTurno}/cancelar-invitado")
    public ResponseEntity<TurnoResponse> cancelarTurnoInvitado(
            @PathVariable Integer idTurno,
            @RequestParam Integer idUsuario
    ) {
        var turno = turnoService.cancelarTurnoInvitado(idTurno, idUsuario);
        return ResponseEntity.ok(TurnoResponse.fromEntity(turno));
    }
}
