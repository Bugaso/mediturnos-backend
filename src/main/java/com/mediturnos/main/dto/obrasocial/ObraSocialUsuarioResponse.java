package com.mediturnos.main.dto.obrasocial;

import com.mediturnos.main.data.obrasocial.ObraSocialUsuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObraSocialUsuarioResponse {
    private Integer idUsuario;
    private Integer idObraSocial;
    private String nombreObra;
    private String planAfiliado;
    private String nroCredencial;

    public static ObraSocialUsuarioResponse fromEntity(ObraSocialUsuario osu) {
        return ObraSocialUsuarioResponse.builder()
                .idUsuario(osu.getUsuario().getIdUsuario())
                .idObraSocial(osu.getObraSocial().getIdObraSocial())
                .nombreObra(osu.getObraSocial().getNombreObra())
                .planAfiliado(osu.getPlanAfiliado())
                .nroCredencial(osu.getNroCredencial())
                .build();
    }
}