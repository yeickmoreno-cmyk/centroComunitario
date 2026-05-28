package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.service.interfaces.IActividadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la colección 'actividades'.
 * Base URL: /centrocomunitario/api/actividades
 */
@RestController
@RequestMapping("/actividades")
@RequiredArgsConstructor
public class ActividadController {

    private final IActividadService actividadService;

    // POST /actividades/insertar
    @PostMapping("/insertar")
    public ResponseEntity<ActividadModel> crear(@Valid @RequestBody ActividadModel actividad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(actividadService.crear(actividad));
    }

    // GET /actividades/listar
    @GetMapping("/listar")
    public ResponseEntity<List<ActividadModel>> listar() {
        return ResponseEntity.ok(actividadService.listarTodos());
    }

    // GET /actividades/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ActividadModel> buscarPorId(@PathVariable String id) {
        return actividadService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /actividades/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ActividadModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody ActividadModel actividad) {
        return ResponseEntity.ok(actividadService.actualizar(id, actividad));
    }

    // GET /actividades/categoria/{categoria}
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ActividadModel>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(actividadService.buscarPorCategoria(categoria));
    }

    // GET /actividades/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ActividadModel>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(actividadService.buscarPorEstado(estado));
    }

    // GET /actividades/con-cupos
    @GetMapping("/con-cupos")
    public ResponseEntity<List<ActividadModel>> listarConCupos() {
        return ResponseEntity.ok(actividadService.listarConCuposDisponibles());
    }

//    // DELETE /actividades/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        actividadService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
