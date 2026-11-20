package com.mediturnos.main.data.medico;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mediturnos.main.data.turno.Turno;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Especialidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer idEspecialidad;

    @Column(name = "nombre_especialidad", nullable = false, unique = true, length = 100)
    private String nombreEspecialidad;

    @Builder.Default
    @Column(name = "estado")
    private Boolean estado = true;

    @JsonIgnore
    @Builder.Default
    @ManyToMany(mappedBy = "especialidades")
    private Set<Medico> medicos = new HashSet<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "especialidad")
    private List<Turno> turnos = new ArrayList<>();
}
