package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Documento embebido dentro de la colección 'actividades'.
 * Representa un recurso requerido para la actividad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoRequerido {

    @NotBlank(message = "El nombre del recurso es obligatorio")
    private String nombre;

    @NotNull(message = "La cantidad es obligatoria")
    private Integer cantidad;

    @NotBlank(message = "El tipo de recurso es obligatorio")
    private String tipo;

    private String descripcion;

    private String ubicacion;

    private String estado;
}
