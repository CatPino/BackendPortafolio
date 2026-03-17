package com.backend.backend_usuario.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SolicitudCrearUsuario(
    @NotBlank
    @Size(min = 2, max = 120)
    String nombre,

    @NotBlank
    @Email
    @Size(max = 180)
    String email,

    @NotBlank
    @Size(min = 8, max = 100)
    String password,
    
    @NonNull
    @Size(max = 20)
    String telefono,

    @NotBlank
    @Size(max = 100)
    String region,

    @NotBlank
    @Size(max = 100)
    String comuna
) {}