package com.backend.backend_usuario.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ActualizarPerfil {
    @Size(min = 2, max = 120)
        String nombre;

    @Size(max = 20)
        String telefono;

    @Size(max = 100)
    String region;

    @Size(max = 100)
    String comuna;

    @Size(max = 200)
    String direccion;

    @Size(max = 50)
    String departamento;

    @Size(max = 255)
    String infoEnvio;

}
