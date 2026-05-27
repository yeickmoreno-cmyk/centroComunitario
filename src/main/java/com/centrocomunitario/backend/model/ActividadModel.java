package com.centrocomunitario.backend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

/**
 * Colección: actividades
 * Categorías: arte, deporte, tecnologia, salud, emprendimiento, desarrollo personal
 * Estados: pendiente_revision, aprobada, programada, en_curso, finalizada, cancelada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "actividades")
public class ActividadModel {

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    @Pattern(
        regexp = "arte|deporte|tecnologia|salud|emprendimiento|desarrollo personal",
        message = "Categoría inválida"
    )
    private String categoria;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El objetivo es obligatorio")
    private String objetivo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Field("fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Field("fecha_fin")
    private LocalDate fechaFin;

    @NotNull(message = "La intensidad en horas es obligatoria")
    @Field("intensidad_horas")
    private Double intensidadHoras;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    @Field("cupo_maximo")
    private Integer cupoMaximo;

    @NotNull(message = "Los cupos disponibles son obligatorios")
    @Field("cupos_disponibles")
    private Integer cuposDisponibles;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(
        regexp = "pendiente_revision|aprobada|programada|en_curso|finalizada|cancelada",
        message = "Estado inválido"
    )
    private String estado;

    /** FK -> usuarios._id */
    @Field("propuesto_por")
    private String propuestoPor;

    @Valid
    @Field("recursos_requeridos")
    private List<RecursoRequerido> recursosRequeridos;

    /** FK[] -> sesiones._id */
    @Field("sesiones_id")
    private List<String> sesionesId;

    @NotNull(message = "La fecha de creación es obligatoria")
    @Field("fecha_creacion")
    private LocalDate fechaCreacion;
}
