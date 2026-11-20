package com.mediturnos.main.dto.usuario;

import com.mediturnos.main.data.usuario.Rol;
import com.mediturnos.main.data.usuario.Usuario;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PerfilResponse {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String contactoEmergencia;
    private String email;
    private Boolean usuarioActivo;
    private List<String> roles;

    public static PerfilResponse fromEntity(Usuario u) {
        return PerfilResponse.builder()
                .idUsuario(u.getIdUsuario())
                .nombre(u.getNombre())
                .apellido(u.getApellido())
                .dni(u.getDni())
                .telefono(u.getTelefono())
                .contactoEmergencia(u.getContactoEmergencia())
                .email(u.getEmail())
                .usuarioActivo(u.getUsuarioActivo())
                .roles(u.getRoles().stream()
                        .map(Rol::getNombreRol)
                        .toList())
                .build();
    }
}
