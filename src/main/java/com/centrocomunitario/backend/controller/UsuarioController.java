package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.Notificacion;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.service.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base URL: /centrocomunitario/api/usuarios
 */
@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final IUsuarioService usuarioService;

    // POST /usuarios/insertar
    @PostMapping("/insertar")
    public ResponseEntity<UsuarioModel> crear(@Valid @RequestBody UsuarioModel usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(usuario));
    }

    // GET /usuarios/listar
    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioModel>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    // GET /usuarios/buscar/{id}
    @GetMapping("/buscar/{id}")
    public ResponseEntity<UsuarioModel> buscarPorId(@PathVariable String id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /usuarios/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody UsuarioModel usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    // GET /usuarios/{id}/notificaciones
    @GetMapping("/{id}/notificaciones")
    public ResponseEntity<List<Notificacion>> listarNotificaciones(@PathVariable String id) {
        return ResponseEntity.ok(usuarioService.listarNotificaciones(id));
    }

    // PUT /usuarios/{id}/notificaciones/{nid}/leer
    @PutMapping("/{id}/notificaciones/{nid}/leer")
    public ResponseEntity<UsuarioModel> marcarLeida(
            @PathVariable String id,
            @PathVariable String nid) {
        return ResponseEntity.ok(usuarioService.marcarLeida(id, nid));
    }

    // GET /usuarios/rol/{rol}
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioModel>> buscarPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(rol));
    }

    // GET /usuarios/nombre?q={q}
    @GetMapping("/nombre")
    public ResponseEntity<List<UsuarioModel>> buscarPorNombre(@RequestParam("q") String q) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(q));
    }

//    // DELETE /usuarios/eliminar/{id}
//    @DeleteMapping("/eliminar/{id}")
//    public ResponseEntity<Void> eliminar(@PathVariable String id) {
//        usuarioService.eliminar(id);
//        return ResponseEntity.noContent().build();
//    }
}
