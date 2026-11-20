package com.mediturnos.main.controladores.medico;

import com.mediturnos.main.dto.especialidad.EspecialidadResponse;
import com.mediturnos.main.servicios.medico.EspecialidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/especialidades")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;

    @GetMapping
    public ResponseEntity<List<EspecialidadResponse>> listarActivas() {
        List<EspecialidadResponse> lista = especialidadService.listarActivas().stream()
                .map(EspecialidadResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/todas")
    public ResponseEntity<List<EspecialidadResponse>> listarTodas() {
        List<EspecialidadResponse> lista = especialidadService.listarTodas().stream()
                .map(EspecialidadResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadResponse> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(
                EspecialidadResponse.fromEntity(especialidadService.obtenerPorId(id))
        );
    }
}
