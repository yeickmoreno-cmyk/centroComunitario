package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.service.interfaces.IInscripcionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base URL: /centrocomunitario/api/inscripciones
 */
@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final IInscripcionService inscripcionService;

    // POST /inscripciones/insertar
    @PostMapping("/insertar")
    public ResponseEntity<InscripcionModel> crear(@Valid @RequestBody InscripcionModel inscripcion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcionService.crear(inscripcion));
    }

    // GET /inscripciones/listar
    @GetMapping("/listar")
    public ResponseEntity<List<InscripcionModel>> listar() {
        return ResponseEntity.ok(inscripcionService.listarTodos());
    }

    // GET /inscripciones/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<InscripcionModel> buscarPorId(@PathVariable String id) {
        return inscripcionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /inscripciones/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<InscripcionModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody InscripcionModel inscripcion) {
        return ResponseEntity.ok(inscripcionService.actualizar(id, inscripcion));
    }

    // GET /inscripciones/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<InscripcionModel>> buscarPorUsuario(@PathVariable String usuarioId) {
        return ResponseEntity.ok(inscripcionService.buscarPorUsuario(usuarioId));
    }

    // GET /inscripciones/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<InscripcionModel>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(inscripcionService.buscarPorEstado(estado));
    }

//    // DELETE /inscripciones/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        inscripcionService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
