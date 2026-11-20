package com.mediturnos.main.servicios.usuario;

import com.mediturnos.main.data.usuario.Usuario;
import com.mediturnos.main.repositorios.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        if (Boolean.FALSE.equals(usuario.getUsuarioActivo())) {
            throw new UsernameNotFoundException("El usuario se encuentra inactivo");
        }

        List<SimpleGrantedAuthority> authorities = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombreRol().toUpperCase()))
                .toList();

        return new User(
                usuario.getEmail(),
                usuario.getPassword() != null ? usuario.getPassword() : "",
                authorities
        );
    }
}
