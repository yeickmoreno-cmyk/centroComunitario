package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.Notificacion;
import com.centrocomunitario.backend.model.ProgramaModel;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.IProgramas;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final IInscripciones inscripcionRepository;
    private final IProgramas programaRepository;

    @Override
    public UsuarioModel crear(UsuarioModel usuario) {
        if (usuarioRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuario.getCorreo());
        }
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

        String estadoAnterior = existente.getEstado();

        usuarioRepository.findByCorreo(usuario.getCorreo()).ifPresent(otro -> {
            if (!otro.getId().equals(id)) {
                throw new IllegalArgumentException(
                        "Ya existe otro usuario con el correo: " + usuario.getCorreo());
            }
        });

        existente.setNombreCompleto(usuario.getNombreCompleto());
        existente.setEdad(usuario.getEdad());
        existente.setCorreo(usuario.getCorreo());
        existente.setTelefono(usuario.getTelefono());
        existente.setDireccion(usuario.getDireccion());
        existente.setRol(usuario.getRol());
        existente.setEstado(usuario.getEstado());
        existente.setNotificaciones(usuario.getNotificaciones());

        // Cascada: desactivar usuario
        if (!"inactivo".equals(estadoAnterior) && "inactivo".equals(existente.getEstado())) {
            List<InscripcionModel> inscripciones = inscripcionRepository.findByUsuarioIdAndEstado(id, "activa");
            for (InscripcionModel inscripcion : inscripciones) {
                inscripcion.setEstado("cancelada");
                inscripcion.setFechaCancelacion(LocalDate.now());
                inscripcion.setMotivoCancelacion("Usuario desactivado");
                inscripcionRepository.save(inscripcion);
            }
            List<ProgramaModel> programas = programaRepository.findByResponsablesIdContaining(id);
            for (ProgramaModel programa : programas) {
                programa.getResponsablesId().remove(id);
                programaRepository.save(programa);
            }
        }

        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public List<Notificacion> listarNotificaciones(String usuarioId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + usuarioId));
        List<Notificacion> notificaciones = usuario.getNotificaciones();
        return notificaciones != null ? notificaciones : new ArrayList<>();
    }

    @Override
    public UsuarioModel marcarLeida(String usuarioId, String notificacionId) {
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + usuarioId));

        if (usuario.getNotificaciones() == null) {
            throw new NoSuchElementException("No se encontró la notificación con id: " + notificacionId);
        }

        usuario.getNotificaciones().stream()
                .filter(n -> notificacionId.equals(n.getNotificacionId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(
                        "No se encontró la notificación con id: " + notificacionId))
                .setLeida(true);

        return usuarioRepository.save(usuario);
    }
}
