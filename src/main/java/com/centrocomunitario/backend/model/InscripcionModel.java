package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * tipo_inscripcion: actividad, programa
 * estado: activa, cancelada, completada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "inscripciones")
public class InscripcionModel {

    @Id
    private String id;

    /** FK -> usuarios._id */
    @NotBlank(message = "El usuario_id es obligatorio")
    @Field("usuario_id")
    private String usuarioId;

    @NotBlank(message = "El tipo de inscripción es obligatorio")
    @Pattern(
        regexp = "actividad|programa",
        message = "tipo_inscripcion inválido. Valores: actividad, programa"
    )
    @Field("tipo_inscripcion")
    private String tipoInscripcion;

    /** FK -> actividades._id o programas._id según tipo_inscripcion */
    @NotBlank(message = "El referencia_id es obligatorio")
    @Field("referencia_id")
    private String referenciaId;

    @NotNull(message = "La fecha de inscripción es obligatoria")
    @Field("fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
        regexp = "activa|cancelada|completada",
        message = "Estado inválido. Valores: activa, cancelada, completada"
    )
    private String estado;

    @Field("fecha_cancelacion")
    private LocalDate fechaCancelacion;

    @Field("motivo_cancelacion")
    private String motivoCancelacion;

    private Double progreso;
}
