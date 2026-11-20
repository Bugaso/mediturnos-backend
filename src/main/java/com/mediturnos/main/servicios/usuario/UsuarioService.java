package com.mediturnos.main.servicios.usuario;

import com.mediturnos.main.data.usuario.Rol;
import com.mediturnos.main.data.usuario.Usuario;
import com.mediturnos.main.repositorios.usuario.RolRepository;
import com.mediturnos.main.repositorios.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public Usuario obtenerPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + idUsuario));
    }

    public Usuario obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    @Transactional
    public Usuario actualizarDatosContacto(
            Integer idUsuario,
            String nombre,
            String apellido,
            String telefono,
            String contactoEmergencia
    ) {
        Usuario usuario = obtenerPorId(idUsuario);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setTelefono(telefono);
        usuario.setContactoEmergencia(contactoEmergencia);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario asignarRol(Integer idUsuario, String nombreRol) {
        Usuario usuario = obtenerPorId(idUsuario);
        Rol rol = rolRepository.findByNombreRol(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));
        usuario.getRoles().add(rol);
        return usuarioRepository.save(usuario);
    }
}