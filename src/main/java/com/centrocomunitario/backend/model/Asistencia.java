package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Documento embebido dentro de la colección 'sesiones'.
 * Registra la asistencia de un usuario a una sesión.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asistencia {

    @NotNull(message = "El usuario_id es obligatorio")
    private String usuarioId;

    @NotNull(message = "El campo convocado es obligatorio")
    private Boolean convocado;

    @NotNull(message = "El campo asistio es obligatorio")
    private Boolean asistio;

    private String horaEntrada;
}
