package com.mediturnos.main.dto.obrasocial;

import com.mediturnos.main.data.obrasocial.ObraSocial;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObraSocialResponse {
    private Integer idObraSocial;
    private String nombreObra;
    private String descripcionObra;
    private Boolean estado;

    public static ObraSocialResponse fromEntity(ObraSocial o) {
        return ObraSocialResponse.builder()
                .idObraSocial(o.getIdObraSocial())
                .nombreObra(o.getNombreObra())
                .descripcionObra(o.getDescripcionObra())
                .estado(o.getEstado())
                .build();
    }
}