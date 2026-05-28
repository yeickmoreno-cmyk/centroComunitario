package com.centrocomunitario.backend.dto;

import lombok.*;

/**
 * DTO que representa el promedio de valoración numérica de una actividad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromedioValoracionDto {

    private String actividadId;
    private Double promedio;
}
