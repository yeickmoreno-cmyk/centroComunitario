package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.dto.EstadisticasDto;
import com.centrocomunitario.backend.service.interfaces.IEstadisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Base URL: /centrocomunitario/api/estadisticas
 * Uso previsto: administradores.
 */
@RestController
@RequestMapping("/estadisticas")
@RequiredArgsConstructor
public class EstadisticaController {

    private final IEstadisticaService estadisticaService;

    // GET /estadisticas/resumen
    @GetMapping("/resumen")
    public ResponseEntity<EstadisticasDto> resumen() {
        return ResponseEntity.ok(estadisticaService.obtenerResumen());
    }
}
