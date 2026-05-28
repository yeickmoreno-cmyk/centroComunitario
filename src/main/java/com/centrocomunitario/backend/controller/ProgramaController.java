package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.ProgramaModel;
import com.centrocomunitario.backend.service.interfaces.IProgramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base URL: /centrocomunitario/api/programas
 */
@RestController
@RequestMapping("/programas")
@RequiredArgsConstructor
public class ProgramaController {

    private final IProgramaService programaService;

    // POST /programas/insertar
    @PostMapping("/insertar")
    public ResponseEntity<ProgramaModel> crear(@Valid @RequestBody ProgramaModel programa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(programaService.crear(programa));
    }

    // GET /programas/listar
    @GetMapping("/listar")
    public ResponseEntity<List<ProgramaModel>> listar() {
        return ResponseEntity.ok(programaService.listarTodos());
    }

    // GET /programas/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<ProgramaModel> buscarPorId(@PathVariable String id) {
        return programaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /programas/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProgramaModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody ProgramaModel programa) {
        return ResponseEntity.ok(programaService.actualizar(id, programa));
    }

    // GET /programas/estado/{estado}
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ProgramaModel>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(programaService.buscarPorEstado(estado));
    }

//    // DELETE /programas/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        programaService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
