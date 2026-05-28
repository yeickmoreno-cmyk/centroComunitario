package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

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

    /** Identificador único generado automáticamente para poder marcar como leída */
    @Field("notificacion_id")
    private String notificacionId;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String mensaje;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El campo 'leida' es obligatorio")
    private Boolean leida;
}
