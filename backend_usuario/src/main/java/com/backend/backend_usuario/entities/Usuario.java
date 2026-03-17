package com.backend.backend_usuario.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "usuarios",
    indexes = {
        @Index(name = "idx_usuarios_rol_estado", columnList = "rol_id, estado"),
        @Index(name = "idx_usuarios_rol_estado_fecha", columnList = "rol_id, estado, fecha_creacion")
    }
)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"password"})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Size(min = 2, max = 120)
    @Column(nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Email
    @Size(max = 180)
    @Column(nullable = false, length = 180, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, max = 100)
    @Column(nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @Size(max = 20)
    private String telefono;

    @Size(max = 100)
    private String region;

    @Size(max = 100)
    private String comuna;

    @Size(max = 200)
    private String direccion;  

    @Size(max = 50)
    private String departamento; 

    @Size(max = 255)
    private String infoEnvio; 

    @Column(name = "estado", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean estado = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    @JsonBackReference   
    private Rol rol;

    @JsonProperty("rol_id")
    @Column(name = "rol_id", insertable = false, updatable = false)
    private Long rolId;

    @PrePersist
    public void prePersist() {
        if (email != null) email = email.trim().toLowerCase();
    }

    @PreUpdate
    public void preUpdate() {
        if (email != null) email = email.trim().toLowerCase();
    }
}
