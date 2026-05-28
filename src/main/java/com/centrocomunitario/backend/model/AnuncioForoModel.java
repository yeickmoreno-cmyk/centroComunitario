package com.centrocomunitario.backend.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "anuncios_foros")
public class AnuncioForoModel {

    @Id
    private String id;

    @NotBlank(message = "El tipo es obligatorio")
    private String tipo;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    private String contenido;

    @NotNull(message = "La fecha de publicación es obligatoria")
    @Field("fecha_publicacion")
    private LocalDate fechaPublicacion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @Valid
    private List<Adjunto> adjuntos;

    @Valid
    private List<Comentario> comentarios;

    /** FK -> usuarios._id */
    @NotBlank(message = "El autor_id es obligatorio")
    @Field("autor_id")
    private String autorId;

    /** FK -> actividades._id */
    @Field("actividad_id")
    private String actividadId;

    /** FK -> programas._id */
    @Field("programa_id")
    private String programaId;
}
