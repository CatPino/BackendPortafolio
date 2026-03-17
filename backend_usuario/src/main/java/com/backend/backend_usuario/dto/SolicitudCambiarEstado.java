package com.backend.backend_usuario.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitudCambiarEstado(
    @NotNull
    Boolean estado
) {}