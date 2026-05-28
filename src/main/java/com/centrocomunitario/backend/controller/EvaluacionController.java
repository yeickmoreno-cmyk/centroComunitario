package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.EvaluacionModel;
import com.centrocomunitario.backend.service.interfaces.IEvaluacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la colección 'evaluaciones'.
 * Base URL: /centrocomunitario/api/evaluaciones
 */
@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final IEvaluacionService evaluacionService;

    // POST /evaluaciones/insertar
    @PostMapping("/insertar")
    public ResponseEntity<EvaluacionModel> crear(@Valid @RequestBody EvaluacionModel evaluacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionService.crear(evaluacion));
    }

    // GET /evaluaciones/listar
    @GetMapping("/listar")
    public ResponseEntity<List<EvaluacionModel>> listar() {
        return ResponseEntity.ok(evaluacionService.listarTodos());
    }

    // GET /evaluaciones/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<EvaluacionModel> buscarPorId(@PathVariable String id) {
        return evaluacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /evaluaciones/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EvaluacionModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody EvaluacionModel evaluacion) {
        return ResponseEntity.ok(evaluacionService.actualizar(id, evaluacion));
    }

//    // DELETE /evaluaciones/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        evaluacionService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
