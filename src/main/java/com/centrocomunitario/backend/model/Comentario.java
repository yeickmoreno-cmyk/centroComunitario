package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comentario {

    @NotNull(message = "El comentarios (id referencia) es obligatorio")
    private String comentarios;

    @NotNull(message = "El autor_id es obligatorio")
    private String autorId;

    @NotBlank(message = "El texto del comentario es obligatorio")
    private String texto;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private List<String> respuesta;
}
