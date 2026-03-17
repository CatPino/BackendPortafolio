package com.backend.backend_usuario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitudCrearRol(
        @NotBlank
        @Size(min = 3, max = 50)
        String nombre,

        @Size(max = 200)
        String descripcion
) {}