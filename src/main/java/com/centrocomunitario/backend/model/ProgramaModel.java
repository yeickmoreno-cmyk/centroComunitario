package com.centrocomunitario.backend.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

/**
 * Colección: programas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "programas")
public class ProgramaModel {

    @Id
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Field("fecha_inicio")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Field("fecha_fin")
    private LocalDate fechaFin;

    @Field("poblacion_objetivo")
    private String poblacionObjetivo;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "La fecha de creación es obligatoria")
    @Field("fecha_creacion")
    private LocalDate fechaCreacion;

    /** FK[] -> usuarios._id */
    @NotEmpty(message = "Debe haber al menos un responsable")
    @Field("responsables_id")
    private List<String> responsablesId;

    /** FK[] -> actividades._id */
    @Field("actividades_id")
    private List<String> actividadesId;
}
