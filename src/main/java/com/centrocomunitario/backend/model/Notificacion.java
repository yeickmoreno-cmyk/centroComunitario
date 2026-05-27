package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

/**
 * Documento embebido dentro de la colección 'usuarios'.
 * Representa una notificación individual del usuario.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El campo 'leida' es obligatorio")
    private Boolean leida;
}
