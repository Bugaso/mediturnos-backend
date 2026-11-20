package com.mediturnos.main.data.medico;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mediturnos.main.data.turno.Turno;
import com.mediturnos.main.data.usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Medico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medico")
    private Integer idMedico;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "matricula", nullable = false, unique = true, length = 50)
    private String matricula;

    @Builder.Default
    @Column(name = "medico_activo")
    private Boolean medicoActivo = true;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "Medico_Especialidad",
            joinColumns = @JoinColumn(name = "id_medico"),
            inverseJoinColumns = @JoinColumn(name = "id_especialidad")
    )
    private Set<Especialidad> especialidades = new HashSet<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HorarioMedico> horarios = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "medico")
    private List<Turno> turnos = new ArrayList<>();
}
