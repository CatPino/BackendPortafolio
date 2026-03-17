package com.backend.backend_usuario.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Size(max = 200)
    @Column(length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private List<Usuario> usuarios;

    @PrePersist @PreUpdate
    public void normalize() {
        if (nombre != null) nombre = nombre.trim().toLowerCase();
    }
}