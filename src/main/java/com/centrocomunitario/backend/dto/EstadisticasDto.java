package com.centrocomunitario.backend.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * DTO de respuesta para el resumen estadístico del administrador.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticasDto {

    /** Total de usuarios agrupados por rol */
    private Map<String, Long> usuariosPorRol;

    /** Total de actividades agrupadas por estado */
    private Map<String, Long> actividadesPorEstado;

    /** Total de inscripciones agrupadas por estado */
    private Map<String, Long> inscripcionesPorEstado;

    /** Promedio de valoración numérica por actividad */
    private List<PromedioValoracionDto> promedioValoracionPorActividad;
}
