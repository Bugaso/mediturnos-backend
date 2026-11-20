package com.mediturnos.main.data.usuario;


import com.mediturnos.main.data.medico.Medico;
import com.mediturnos.main.data.obrasocial.ObraSocialUsuario;
import com.mediturnos.main.data.turno.Turno;
import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    @Column(name = "dni", nullable = false, unique = true, length = 20)
    private String dni;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "contacto_emergencia", length = 20)
    private String contactoEmergencia;

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 255)
    private String password;

    @Builder.Default
    @Column(name = "usuario_activo")
    private Boolean usuarioActivo = true;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Rol_Usuario",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ObraSocialUsuario> obrasSociales = new ArrayList<>();

    @JsonIgnore
    @OneToOne(mappedBy = "usuario")
    private Medico medico;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "usuario")
    private List<Turno> turnos = new ArrayList<>();
}