package com.mediturnos.main.servicios.usuario;

import com.mediturnos.main.data.obrasocial.ObraSocial;
import com.mediturnos.main.data.obrasocial.ObraSocialUsuario;
import com.mediturnos.main.data.obrasocial.ObraSocialUsuarioId;
import com.mediturnos.main.data.usuario.Usuario;
import com.mediturnos.main.repositorios.obrasocial.ObraSocialRepository;
import com.mediturnos.main.repositorios.obrasocial.ObraSocialUsuarioRepository;
import com.mediturnos.main.repositorios.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ObraSocialService {

    private final ObraSocialRepository obraSocialRepository;
    private final ObraSocialUsuarioRepository obraSocialUsuarioRepository;
    private final UsuarioRepository usuarioRepository;

    public List<ObraSocial> listarActivas() {
        return obraSocialRepository.findByEstadoTrue();
    }

    public List<ObraSocialUsuario> listarPorUsuario(Integer idUsuario) {
        return obraSocialUsuarioRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Transactional
    public ObraSocialUsuario asociarAUsuario(
            Integer idUsuario,
            Integer idObraSocial,
            String planAfiliado,
            String nroCredencial
    ) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ObraSocial obraSocial = obraSocialRepository.findById(idObraSocial)
                .orElseThrow(() -> new RuntimeException("Obra social no encontrada"));

        ObraSocialUsuarioId id = new ObraSocialUsuarioId(idUsuario, idObraSocial);

        ObraSocialUsuario asociacion = obraSocialUsuarioRepository.findById(id)
                .orElse(ObraSocialUsuario.builder()
                        .id(id)
                        .usuario(usuario)
                        .obraSocial(obraSocial)
                        .build());

        asociacion.setPlanAfiliado(planAfiliado);
        asociacion.setNroCredencial(nroCredencial);

        return obraSocialUsuarioRepository.save(asociacion);
    }

    @Transactional
    public void desvincularDeUsuario(Integer idUsuario, Integer idObraSocial) {
        ObraSocialUsuarioId id = new ObraSocialUsuarioId(idUsuario, idObraSocial);
        if (!obraSocialUsuarioRepository.existsById(id)) {
            throw new RuntimeException("La obra social no está asociada a este usuario");
        }
        obraSocialUsuarioRepository.deleteById(id);
    }
}
