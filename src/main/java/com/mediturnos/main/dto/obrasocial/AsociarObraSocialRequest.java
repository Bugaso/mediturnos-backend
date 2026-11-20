package com.mediturnos.main.dto.obrasocial;

import lombok.Data;

@Data
public class AsociarObraSocialRequest {
    private Integer idUsuario;
    private Integer idObraSocial;
    private String planAfiliado;
    private String nroCredencial;
}