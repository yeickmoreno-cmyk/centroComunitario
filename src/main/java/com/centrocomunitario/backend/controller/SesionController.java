package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.SesionModel;
import com.centrocomunitario.backend.service.interfaces.ISesionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base URL: /centrocomunitario/api/sesiones
 */
@RestController
@RequestMapping("/sesiones")
@RequiredArgsConstructor
public class SesionController {

    private final ISesionService sesionService;

    // POST /sesiones/insertar
    @PostMapping("/insertar")
    public ResponseEntity<SesionModel> crear(@Valid @RequestBody SesionModel sesion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sesionService.crear(sesion));
    }

    // GET /sesiones/listar
    @GetMapping("/listar")
    public ResponseEntity<List<SesionModel>> listar() {
        return ResponseEntity.ok(sesionService.listarTodos());
    }

    // GET /sesiones/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<SesionModel> buscarPorId(@PathVariable String id) {
        return sesionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /sesiones/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<SesionModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody SesionModel sesion) {
        return ResponseEntity.ok(sesionService.actualizar(id, sesion));
    }

    // PUT /sesiones/{id}/asignar-instructor?instructorId=xxx
    @PutMapping("/{id}/asignar-instructor")
    public ResponseEntity<SesionModel> asignarInstructor(
            @PathVariable String id,
            @RequestParam String instructorId) {
        return ResponseEntity.ok(sesionService.asignarInstructor(id, instructorId));
    }

    // GET /sesiones/horario/{usuarioId}
    @GetMapping("/horario/{usuarioId}")
    public ResponseEntity<List<SesionModel>> horarioParticipante(@PathVariable String usuarioId) {
        return ResponseEntity.ok(sesionService.horarioParticipante(usuarioId));
    }

    // GET /sesiones/actividad/{actividadId}
    @GetMapping("/actividad/{actividadId}")
    public ResponseEntity<List<SesionModel>> buscarPorActividad(@PathVariable String actividadId) {
        return ResponseEntity.ok(sesionService.buscarPorActividad(actividadId));
    }

//    // DELETE /sesiones/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        sesionService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
