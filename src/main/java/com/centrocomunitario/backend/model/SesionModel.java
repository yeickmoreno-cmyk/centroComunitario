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
 * Colección: sesiones
 * Modalidades: presencial, virtual, hibrida
 * Estados: programada, en_curso, finalizada, cancelada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sesiones")
public class SesionModel {

    @Id
    private String id;

    /** FK -> actividades._id */
    @NotBlank(message = "El actividad_id es obligatorio")
    @Field("actividad_id")
    private String actividadId;

    @NotNull(message = "El número de sesión es obligatorio")
    @Field("numero_sesion")
    private Integer numeroSesion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La hora de inicio es obligatoria")
    @Field("hora_inicio")
    private String horaInicio;

    @NotBlank(message = "La hora de fin es obligatoria")
    @Field("hora_fin")
    private String horaFin;

    @NotBlank(message = "La modalidad es obligatoria")
    @Pattern(
        regexp = "presencial|virtual|hibrida",
        message = "Modalidad inválida. Valores: presencial, virtual, hibrida"
    )
    private String modalidad;

    @Field("espacio_o_enlace")
    private String espacioOEnlace;

    private String observaciones;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Valid
    private List<Asistencia> asistencia;

    @Valid
    private List<Adjunto> adjuntos;
}
