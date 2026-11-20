package com.mediturnos.main.data.obrasocial;

import com.mediturnos.main.data.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ObraSocial_Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObraSocialUsuario {

    @EmbeddedId
    @Builder.Default
    private ObraSocialUsuarioId id = new ObraSocialUsuarioId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idObraSocial")
    @JoinColumn(name = "id_obra_social", nullable = false)
    private ObraSocial obraSocial;

    @Column(name = "plan_afiliado", length = 100)
    private String planAfiliado;

    @Column(name = "nro_credencial", length = 50)
    private String nroCredencial;
}
