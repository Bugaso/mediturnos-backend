package com.mediturnos.main.controladores.usuario;

import com.mediturnos.main.dto.usuario.ActualizarPerfilRequest;
import com.mediturnos.main.dto.usuario.PerfilResponse;
import com.mediturnos.main.servicios.usuario.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<PerfilResponse> obtenerPerfilAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(
                PerfilResponse.fromEntity(usuarioService.obtenerPorEmail(email))
        );
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<PerfilResponse> obtenerPorId(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(
                PerfilResponse.fromEntity(usuarioService.obtenerPorId(idUsuario))
        );
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<PerfilResponse> actualizarDatosContacto(
            @PathVariable Integer idUsuario,
            @RequestBody ActualizarPerfilRequest request
    ) {
        var usuarioActualizado = usuarioService.actualizarDatosContacto(
                idUsuario,
                request.getNombre(),
                request.getApellido(),
                request.getTelefono(),
                request.getContactoEmergencia()
        );
        return ResponseEntity.ok(PerfilResponse.fromEntity(usuarioActualizado));
    }

    @PostMapping("/{idUsuario}/roles")
    public ResponseEntity<PerfilResponse> asignarRol(
            @PathVariable Integer idUsuario,
            @RequestParam String nombreRol
    ) {
        var usuarioActualizado = usuarioService.asignarRol(idUsuario, nombreRol);
        return ResponseEntity.ok(PerfilResponse.fromEntity(usuarioActualizado));
    }
}
