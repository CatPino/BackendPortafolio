package com.backend.backend_usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioAdmin(
    @Size(min = 2, max = 120)
    String nombre,

    @Email
    @Size(max = 180)
    String email,

    @Size(min = 8, max = 100)
    String password,

    @Size(max = 20, message = "El teléfono no debe superar los 20 caracteres")
    String telefono,

    @Size(max = 80, message = "La región no debe superar los 80 caracteres")
    String region,

    @Size(max = 80, message = "La comuna no debe superar los 80 caracteres")
    String comuna,

    Long rolId,

    String rolNombre,
    
    Boolean estado
) {}