package com.mediturnos.main.controladores.medico;

import com.mediturnos.main.data.enums.DiaSemana;
import com.mediturnos.main.dto.medico.HorarioMedicoResponse;
import com.mediturnos.main.dto.medico.MedicoResponse;
import com.mediturnos.main.servicios.medico.MedicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoService medicoService;

    @GetMapping
    public ResponseEntity<List<MedicoResponse>> listarActivos(
            @RequestParam(required = false) Integer idEspecialidad
    ) {
        var medicos = (idEspecialidad != null)
                ? medicoService.listarPorEspecialidad(idEspecialidad)
                : medicoService.listarActivos();

        return ResponseEntity.ok(medicos.stream()
                .map(MedicoResponse::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(
                MedicoResponse.fromEntity(medicoService.obtenerPorId(id))
        );
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<MedicoResponse> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(
                MedicoResponse.fromEntity(medicoService.obtenerPorUsuario(idUsuario))
        );
    }

    @GetMapping("/{idMedico}/horarios")
    public ResponseEntity<List<HorarioMedicoResponse>> obtenerHorarios(
            @PathVariable Integer idMedico,
            @RequestParam(required = false) DiaSemana diaSemana
    ) {
        var horarios = (diaSemana != null)
                ? medicoService.obtenerHorariosPorDia(idMedico, diaSemana)
                : medicoService.obtenerHorarios(idMedico);

        return ResponseEntity.ok(horarios.stream()
                .map(HorarioMedicoResponse::fromEntity)
                .toList());
    }
}
