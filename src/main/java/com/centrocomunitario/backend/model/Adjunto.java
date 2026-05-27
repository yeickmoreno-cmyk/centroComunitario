package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

/**
 * Documento embebido compartido por 'sesiones' y 'anuncios_foros'.
 * Representa un archivo adjunto.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adjunto {

    @NotBlank(message = "El nombre del adjunto es obligatorio")
    private String nombre;

    @NotBlank(message = "El tipo del adjunto es obligatorio")
    // Valores: pdf, material audiovisual, presentaciones, fotografias, actas de seguimiento
    private String tipo;

    @NotBlank(message = "La URL del adjunto es obligatoria")
    private String url;

    private LocalDate fechaCarga;
}
