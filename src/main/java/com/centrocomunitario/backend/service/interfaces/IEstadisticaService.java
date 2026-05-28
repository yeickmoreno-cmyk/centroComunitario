package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.dto.EstadisticasDto;

/**
 * Contrato para las estadísticas generales del centro (uso del administrador).
 */
public interface IEstadisticaService {

    EstadisticasDto obtenerResumen();
}
