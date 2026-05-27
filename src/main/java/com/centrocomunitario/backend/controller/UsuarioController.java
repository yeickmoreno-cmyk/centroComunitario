package com.centrocomunitario.backend.controller;

import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.service.interfaces.IUsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la colección 'usuarios'.
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

    // GET /usuarios/buscar/correo/{correo}
    @GetMapping("/buscar/correo/{correo}")
    public ResponseEntity<UsuarioModel> buscarPorCorreo(@PathVariable String correo) {
        return usuarioService.buscarPorCorreo(correo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /usuarios/buscar/rol/{rol}
    @GetMapping("/buscar/rol/{rol}")
    public ResponseEntity<List<UsuarioModel>> buscarPorRol(@PathVariable String rol) {
        return ResponseEntity.ok(usuarioService.buscarPorRol(rol));
    }

    // GET /usuarios/buscar/nombre/{nombre}
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<UsuarioModel>> buscarPorNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(usuarioService.buscarPorNombre(nombre));
    }

    // PUT /usuarios/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<UsuarioModel> actualizar(
            @PathVariable String id,
            @Valid @RequestBody UsuarioModel usuario) {
        return ResponseEntity.ok(usuarioService.actualizar(id, usuario));
    }

    // DELETE /usuarios/eliminar/{id}
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
