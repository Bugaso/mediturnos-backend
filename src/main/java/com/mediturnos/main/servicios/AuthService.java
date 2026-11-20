package com.mediturnos.main.servicios;

import com.mediturnos.main.data.usuario.Rol;
import com.mediturnos.main.data.usuario.Usuario;
import com.mediturnos.main.dto.RegisterRequest;
import com.mediturnos.main.dto.usuario.AuthResponse;
import com.mediturnos.main.dto.usuario.LoginRequest;
import com.mediturnos.main.repositorios.usuario.RolRepository;
import com.mediturnos.main.repositorios.usuario.UsuarioRepository;
import com.mediturnos.main.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.email().trim())) {
            throw new RuntimeException("El email ya está registrado");
        }

        Rol rolPaciente = rolRepository.findByNombreRol("Paciente")
                .orElseThrow(() -> new RuntimeException("El rol 'Paciente' no existe en la base de datos"));

        Usuario usuario = usuarioRepository.findByDni(request.dni().trim())
                .map(existente -> {
                    if (existente.getPassword() != null) {
                        throw new RuntimeException("El DNI ya está registrado");
                    }
                    // Completa los datos de acceso del usuario que antes reservó como invitado
                    existente.setNombre(request.nombre());
                    existente.setApellido(request.apellido());
                    existente.setTelefono(request.telefono());
                    existente.setContactoEmergencia(request.contactoEmergencia());
                    existente.setEmail(request.email().trim());
                    existente.setPassword(passwordEncoder.encode(request.password()));
                    return existente;
                })
                .orElseGet(() -> Usuario.builder()
                        .nombre(request.nombre())
                        .apellido(request.apellido())
                        .dni(request.dni().trim())
                        .telefono(request.telefono())
                        .contactoEmergencia(request.contactoEmergencia())
                        .email(request.email().trim())
                        .password(passwordEncoder.encode(request.password()))
                        .usuarioActivo(true)
                        .build());

        // Asignación automática del rol Paciente
        if (usuario.getRoles() == null) {
            usuario.setRoles(new HashSet<>());
        }
        usuario.getRoles().add(rolPaciente);

        usuario = usuarioRepository.save(usuario);

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol().toUpperCase()))
                .toList();

        UserDetails userDetails = new User(usuario.getEmail(), usuario.getPassword(), authorities);
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (Boolean.FALSE.equals(usuario.getUsuarioActivo())) {
            throw new RuntimeException("El usuario se encuentra inactivo");
        }

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol().toUpperCase()))
                .toList();

        UserDetails userDetails = new User(usuario.getEmail(), usuario.getPassword(), authorities);
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .idUsuario(usuario.getIdUsuario())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .build();
    }
}