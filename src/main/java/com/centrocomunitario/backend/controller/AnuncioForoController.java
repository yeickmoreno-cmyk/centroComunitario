package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.AnuncioForoModel;
import com.centrocomunitario.backend.model.Comentario;
import com.centrocomunitario.backend.service.interfaces.IAnuncioForoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la colección 'anuncios_foros'.
 * Base URL: /centrocomunitario/api/anuncios-foros
 */
@RestController
@RequestMapping("/anuncios-foros")
@RequiredArgsConstructor
public class AnuncioForoController {

    private final IAnuncioForoService anuncioForoService;

    // POST /anuncios-foros/insertar
    @PostMapping("/insertar")
    public ResponseEntity<AnuncioForoModel> crear(@Valid @RequestBody AnuncioForoModel anuncio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioForoService.crear(anuncio));
    }

    // GET /anuncios-foros/listar
    @GetMapping("/listar")
    public ResponseEntity<List<AnuncioForoModel>> listar() {
        return ResponseEntity.ok(anuncioForoService.listarTodos());
    }

    // GET /anuncios-foros/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<AnuncioForoModel> buscarPorId(@PathVariable String id) {
        return anuncioForoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /anuncios-foros/buscar/tipo/{tipo}
    @GetMapping("/buscar/tipo/{tipo}")
    public ResponseEntity<List<AnuncioForoModel>> buscarPorTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(anuncioForoService.buscarPorTipo(tipo));
    }

    // GET /anuncios-foros/buscar/autor/{autorId}
    @GetMapping("/buscar/autor/{autorId}")
    public ResponseEntity<List<AnuncioForoModel>> buscarPorAutor(@PathVariable String autorId) {
        return ResponseEntity.ok(anuncioForoService.buscarPorAutor(autorId));
    }

    // GET /anuncios-foros/buscar/actividad/{actividadId}
    @GetMapping("/buscar/actividad/{actividadId}")
    public ResponseEntity<List<AnuncioForoModel>> buscarPorActividad(@PathVariable String actividadId) {
        return ResponseEntity.ok(anuncioForoService.buscarPorActividad(actividadId));
    }

    // GET /anuncios-foros/buscar/titulo/{titulo}
    @GetMapping("/buscar/titulo/{titulo}")
    public ResponseEntity<List<AnuncioForoModel>> buscarPorTitulo(@PathVariable String titulo) {
        return ResponseEntity.ok(anuncioForoService.buscarPorTitulo(titulo));
    }

    // GET /anuncios-foros/{id}/comentarios
    @GetMapping("/{id}/comentarios")
    public ResponseEntity<List<Comentario>> obtenerComentarios(@PathVariable String id) {
        return ResponseEntity.ok(anuncioForoService.obtenerComentarios(id));
    }

    // POST /anuncios-foros/{id}/comentarios/agregar
    @PostMapping("/{id}/comentarios/agregar")
    public ResponseEntity<AnuncioForoModel> agregarComentario(
            @PathVariable String id,
            @Valid @RequestBody Comentario comentario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioForoService.agregarComentario(id, comentario));
    }

    // PUT /anuncios-foros/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<AnuncioForoModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody AnuncioForoModel anuncio) {
        return ResponseEntity.ok(anuncioForoService.actualizar(id, anuncio));
    }

    // DELETE /anuncios-foros/eliminar/{id}
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        anuncioForoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
