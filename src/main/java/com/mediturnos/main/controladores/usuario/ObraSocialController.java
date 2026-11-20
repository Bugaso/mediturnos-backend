package com.mediturnos.main.controladores.usuario;

import com.mediturnos.main.dto.obrasocial.AsociarObraSocialRequest;
import com.mediturnos.main.dto.obrasocial.ObraSocialResponse;
import com.mediturnos.main.dto.obrasocial.ObraSocialUsuarioResponse;
import com.mediturnos.main.servicios.usuario.ObraSocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/obras-sociales")
@RequiredArgsConstructor
public class ObraSocialController {

    private final ObraSocialService obraSocialService;

    @GetMapping
    public ResponseEntity<List<ObraSocialResponse>> listarActivas() {
        List<ObraSocialResponse> lista = obraSocialService.listarActivas().stream()
                .map(ObraSocialResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ObraSocialUsuarioResponse>> listarPorUsuario(@PathVariable Integer idUsuario) {
        List<ObraSocialUsuarioResponse> lista = obraSocialService.listarPorUsuario(idUsuario).stream()
                .map(ObraSocialUsuarioResponse::fromEntity)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/usuario")
    public ResponseEntity<ObraSocialUsuarioResponse> asociarAUsuario(
            @RequestBody AsociarObraSocialRequest request
    ) {
        var asociacion = obraSocialService.asociarAUsuario(
                request.getIdUsuario(),
                request.getIdObraSocial(),
                request.getPlanAfiliado(),
                request.getNroCredencial()
        );
        return ResponseEntity.ok(ObraSocialUsuarioResponse.fromEntity(asociacion));
    }

    @DeleteMapping("/usuario/{idUsuario}/{idObraSocial}")
    public ResponseEntity<Void> desvincularDeUsuario(
            @PathVariable Integer idUsuario,
            @PathVariable Integer idObraSocial
    ) {
        obraSocialService.desvincularDeUsuario(idUsuario, idObraSocial);
        return ResponseEntity.noContent().build();
    }
}
