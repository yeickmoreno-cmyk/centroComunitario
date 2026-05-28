package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.Notificacion;
import com.centrocomunitario.backend.model.ProgramaModel;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IActividades;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.IProgramas;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IInscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImp implements IInscripcionService {

    private final IInscripciones inscripcionRepository;
    private final IUsuarios usuarioRepository;
    private final IActividades actividadRepository;
    private final IProgramas programaRepository;

    @Override
    public InscripcionModel crear(InscripcionModel inscripcion) {
        inscripcionRepository
                .findInscripcionActivaByUsuarioYReferencia(inscripcion.getUsuarioId(), inscripcion.getReferenciaId())
                .ifPresent(i -> {
                    throw new IllegalArgumentException("El usuario ya tiene una inscripción activa en esta referencia");
                });

        // Verificar que el usuario existe
        UsuarioModel usuario = usuarioRepository.findById(inscripcion.getUsuarioId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario no encontrado con id: " + inscripcion.getUsuarioId()));

        // Verificar que el usuario está activo
        if (!"activo".equals(usuario.getEstado())) {
            throw new IllegalArgumentException("No se puede inscribir a un usuario inactivo");
        }

        InscripcionModel guardada;

        if ("actividad".equals(inscripcion.getTipoInscripcion())) {
            // Validaciones para inscripción a actividad
            ActividadModel actividad = actividadRepository.findById(inscripcion.getReferenciaId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Actividad no encontrada con id: " + inscripcion.getReferenciaId()));

            if (!"programada".equals(actividad.getEstado()) && !"en_curso".equals(actividad.getEstado())) {
                throw new IllegalArgumentException("Solo se puede inscribir en actividades programadas o en curso");
            }

            if (actividad.getCuposDisponibles() <= 0) {
                throw new IllegalArgumentException("No hay cupos disponibles para esta actividad");
            }

            actividad.setCuposDisponibles(actividad.getCuposDisponibles() - 1);
            actividadRepository.save(actividad);

            try {
                guardada = inscripcionRepository.save(inscripcion);
            } catch (Exception e) {
                actividad.setCuposDisponibles(actividad.getCuposDisponibles() + 1);
                actividadRepository.save(actividad);
                throw e;
            }

        } else if ("programa".equals(inscripcion.getTipoInscripcion())) {
            // Validaciones para inscripción a programa
            ProgramaModel programa = programaRepository.findById(inscripcion.getReferenciaId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Programa no encontrado con id: " + inscripcion.getReferenciaId()));

            if (!"activo".equals(programa.getEstado())) {
                throw new IllegalArgumentException("Solo se puede inscribir en programas activos");
            }

            guardada = inscripcionRepository.save(inscripcion);
        } else {
            guardada = inscripcionRepository.save(inscripcion);
        }

        // Notificar al usuario sobre su nueva inscripción
        Notificacion notif = Notificacion.builder()
                .notificacionId(UUID.randomUUID().toString())
                .mensaje("Te has inscrito exitosamente. Tipo de inscripción: "
                        + inscripcion.getTipoInscripcion() + ".")
                .fecha(LocalDate.now())
                .leida(false)
                .build();
        if (usuario.getNotificaciones() == null) {
            usuario.setNotificaciones(new ArrayList<>());
        }
        usuario.getNotificaciones().add(notif);
        usuarioRepository.save(usuario);

        return guardada;
    }

    @Override
    public List<InscripcionModel> listarTodos() {
        return inscripcionRepository.findAll();
    }

    @Override
    public Optional<InscripcionModel> buscarPorId(String id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public List<InscripcionModel> buscarPorUsuario(String usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<InscripcionModel> buscarPorReferencia(String referenciaId) {
        return inscripcionRepository.findByReferenciaId(referenciaId);
    }

    @Override
    public List<InscripcionModel> buscarPorEstado(String estado) {
        return inscripcionRepository.findByEstado(estado);
    }

    @Override
    public InscripcionModel actualizar(String id, InscripcionModel inscripcion) {
        InscripcionModel existente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inscripción no encontrada con id: " + id));

        String estadoAnterior = existente.getEstado();

        existente.setEstado(inscripcion.getEstado());
        existente.setProgreso(inscripcion.getProgreso());
        existente.setFechaCancelacion(inscripcion.getFechaCancelacion());
        existente.setMotivoCancelacion(inscripcion.getMotivoCancelacion());

        InscripcionModel guardada = inscripcionRepository.save(existente);

        // Devolver cupo y notificar al usuario al cancelar
        if ("cancelada".equals(existente.getEstado()) && !"cancelada".equals(estadoAnterior)) {
            if ("actividad".equals(existente.getTipoInscripcion())) {
                actividadRepository.findById(existente.getReferenciaId()).ifPresent(actividad -> {
                    actividad.setCuposDisponibles(actividad.getCuposDisponibles() + 1);
                    actividadRepository.save(actividad);
                });
            }
            usuarioRepository.findById(existente.getUsuarioId()).ifPresent(usuario -> {
                Notificacion notif = Notificacion.builder()
                        .notificacionId(UUID.randomUUID().toString())
                        .mensaje("Tu inscripción ha sido cancelada.")
                        .fecha(LocalDate.now())
                        .leida(false)
                        .build();
                if (usuario.getNotificaciones() == null) {
                    usuario.setNotificaciones(new ArrayList<>());
                }
                usuario.getNotificaciones().add(notif);
                usuarioRepository.save(usuario);
            });
        }

        return guardada;
    }

    @Override
    public void eliminar(String id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new NoSuchElementException("Inscripción no encontrada con id: " + id);
        }
        inscripcionRepository.deleteById(id);
    }
}
