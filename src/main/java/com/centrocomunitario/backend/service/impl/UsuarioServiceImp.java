package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementación del servicio de usuarios.
 */
@Service
@RequiredArgsConstructor
public class UsuarioServiceImp implements IUsuarioService {

    private final IUsuarios usuarioRepository;

    @Override
    public UsuarioModel crear(UsuarioModel usuario) {
        // Validar que el correo no exista
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuario.getCorreo());
        }
        // Validar que el documento no exista
        if (usuarioRepository.findByDocumentoId(usuario.getDocumentoId()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el documento: " + usuario.getDocumentoId());
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioModel> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioModel> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<UsuarioModel> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public List<UsuarioModel> buscarPorRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    public List<UsuarioModel> buscarPorNombre(String nombre) {
        return usuarioRepository.buscarPorNombre(nombre);
    }

    @Override
    public UsuarioModel actualizar(String id, UsuarioModel usuario) {
        UsuarioModel existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + id));

        existente.setNombreCompleto(usuario.getNombreCompleto());
        existente.setEdad(usuario.getEdad());
        existente.setCorreo(usuario.getCorreo());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        existente.setRol(usuario.getRol());
        existente.setEstado(usuario.getEstado());
        existente.setNotificaciones(usuario.getNotificaciones());

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
