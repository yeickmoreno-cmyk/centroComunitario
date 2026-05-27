package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

/**
 * Colección: evaluaciones
 * tipo: evaluacion_participante, evaluacion_instructor
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "evaluaciones")
public class EvaluacionModel {

    @Id
    private String id;

    @NotBlank(message = "El tipo es obligatorio")
    @Pattern(
        regexp = "evaluacion_participante|evaluacion_instructor",
        message = "tipo inválido. Valores: evaluacion_participante, evaluacion_instructor"
    )
    private String tipo;

    /** FK -> usuarios._id (quien evalúa) */
    @NotBlank(message = "El autor_id es obligatorio")
    @Field("autor_id")
    private String autorId;

    /** FK -> usuarios._id (quien es evaluado) */
    @Field("sujeto_id")
    private String sujetoId;

    /** FK -> actividades._id */
    @Field("actividad_id")
    private String actividadId;

    /** FK -> sesiones._id */
    @Field("sesion_id")
    private String sesionId;

    @Field("valoracion_numerica")
    private Double valoracionNumerica;

    private String observaciones;

    private String sugerencias;

    private String desempenio;

    private String compromiso;

    @Field("progreso_descrito")
    private String progresoDescrito;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;
}
