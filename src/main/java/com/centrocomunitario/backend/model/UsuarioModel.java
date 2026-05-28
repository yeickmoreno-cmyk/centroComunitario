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
 * Roles disponibles: participante, instructor, coordinador, administrador
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "usuarios")
public class UsuarioModel {

    @Id
    private String id;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Field("nombre_completo")
    private String nombreCompleto;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Field("documento_id")
    private String documentoId;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser mayor a 0")
    private Integer edad;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo debe tener un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    private String direccion;

    @NotBlank(message = "El rol es obligatorio")
    @Pattern(
        regexp = "participante|instructor|coordinador|administrador",
        message = "Rol inválido. Valores permitidos: participante, instructor, coordinador, administrador"
    )
    private String rol;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "La fecha de registro es obligatoria")
    @Field("fecha_registro")
    private LocalDate fechaRegistro;

    @Valid
    private List<Notificacion> notificaciones;
}
