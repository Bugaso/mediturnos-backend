package com.mediturnos.main.repositorios.obrasocial;

import com.mediturnos.main.data.obrasocial.ObraSocial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObraSocialRepository extends JpaRepository<ObraSocial, Integer> {
    List<ObraSocial> findByEstadoTrue();
    Optional<ObraSocial> findByNombreObra(String nombreObra);
}
