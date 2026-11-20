package com.mediturnos.main.data.obrasocial;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ObraSocial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObraSocial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_obra_social")
    private Integer idObraSocial;

    @Column(name = "nombre_obra", nullable = false, unique = true, length = 100)
    private String nombreObra;

    @Column(name = "descripcion_obra", length = 255)
    private String descripcionObra;

    @Builder.Default
    @Column(name = "estado")
    private Boolean estado = true;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "obraSocial", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObraSocialUsuario> usuarios = new ArrayList<>();
}
