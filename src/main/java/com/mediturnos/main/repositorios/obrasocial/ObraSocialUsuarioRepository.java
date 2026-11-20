package com.mediturnos.main.repositorios.obrasocial;

import com.mediturnos.main.data.obrasocial.ObraSocialUsuario;
import com.mediturnos.main.data.obrasocial.ObraSocialUsuarioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObraSocialUsuarioRepository extends JpaRepository<ObraSocialUsuario, ObraSocialUsuarioId> {
    List<ObraSocialUsuario> findByUsuario_IdUsuario(Integer idUsuario);
    List<ObraSocialUsuario> findByObraSocial_IdObraSocial(Integer idObraSocial);
}
