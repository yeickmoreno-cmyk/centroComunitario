package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.Notificacion;
import com.centrocomunitario.backend.model.SesionModel;
import com.centrocomunitario.backend.repository.IActividades;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.ISesiones;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IActividadService;
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
public class ActividadServiceImp implements IActividadService {

    private final IActividades actividadRepository;
    private final IInscripciones inscripcionRepository;
    private final ISesiones sesionRepository;
    private final IUsuarios usuarioRepository;

    @Override
    public ActividadModel crear(ActividadModel actividad) {
        if (actividad.getFechaFin().isBefore(actividad.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (actividad.getCuposDisponibles() > actividad.getCupoMaximo()) {
            throw new IllegalArgumentException("Los cupos disponibles no pueden superar el cupo máximo");
        }
        if (actividad.getPropuestoPor() != null) {
            usuarioRepository.findById(actividad.getPropuestoPor())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Usuario proponente no encontrado con id: "
                            + actividad.getPropuestoPor()));
        }
        return actividadRepository.save(actividad);
    }

    @Override
    public List<ActividadModel> listarTodos() {
        return actividadRepository.findAll();
    }

    @Override
    public Optional<ActividadModel> buscarPorId(String id) {
        return actividadRepository.findById(id);
    }

    @Override
    public List<ActividadModel> buscarPorCategoria(String categoria) {
        return actividadRepository.findByCategoria(categoria);
    }

    @Override
    public List<ActividadModel> buscarPorEstado(String estado) {
        return actividadRepository.findByEstado(estado);
    }

    @Override
    public List<ActividadModel> buscarPorNombre(String nombre) {
        return actividadRepository.buscarPorNombre(nombre);
    }

    @Override
    public List<ActividadModel> listarConCuposDisponibles() {
        return actividadRepository.findConCuposDisponibles();
    }

    @Override
    public ActividadModel actualizar(String id, ActividadModel actividad) {
        ActividadModel existente = actividadRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Actividad no encontrada con id: " + id));

        String estadoAnterior = existente.getEstado();

        existente.setNombre(actividad.getNombre());
        existente.setCategoria(actividad.getCategoria());
        existente.setDescripcion(actividad.getDescripcion());
        existente.setObjetivo(actividad.getObjetivo());
        existente.setFechaInicio(actividad.getFechaInicio());
        existente.setFechaFin(actividad.getFechaFin());
        existente.setIntensidadHoras(actividad.getIntensidadHoras());
        existente.setCupoMaximo(actividad.getCupoMaximo());
        existente.setCuposDisponibles(actividad.getCuposDisponibles());
        existente.setEstado(actividad.getEstado());
        existente.setRecursosRequeridos(actividad.getRecursosRequeridos());

        // Cascada: actividad cancelada
        if (!"cancelada".equals(estadoAnterior) && "cancelada".equals(existente.getEstado())) {
            final String nombreActividad = existente.getNombre();

            List<InscripcionModel> inscripciones = inscripcionRepository.findByReferenciaId(id);
            for (InscripcionModel inscripcion : inscripciones) {
                if ("activa".equals(inscripcion.getEstado())) {
                    inscripcion.setEstado("cancelada");
                    inscripcion.setFechaCancelacion(LocalDate.now());
                    inscripcion.setMotivoCancelacion("Actividad cancelada");
                    inscripcionRepository.save(inscripcion);

                    // Notificar al usuario inscrito
                    usuarioRepository.findById(inscripcion.getUsuarioId()).ifPresent(usuario -> {
                        Notificacion notif = Notificacion.builder()
                                .notificacionId(UUID.randomUUID().toString())
                                .mensaje("La actividad '" + nombreActividad
                                        + "' ha sido cancelada. Tu inscripción fue cancelada automáticamente.")
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
            }

            List<SesionModel> sesiones = sesionRepository.findByActividadId(id);
            for (SesionModel sesion : sesiones) {
                if (!"finalizada".equals(sesion.getEstado()) && !"cancelada".equals(sesion.getEstado())) {
                    sesion.setEstado("cancelada");
                    sesionRepository.save(sesion);
                }
            }
        }

        return actividadRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!actividadRepository.existsById(id)) {
            throw new NoSuchElementException("Actividad no encontrada con id: " + id);
        }
        actividadRepository.deleteById(id);
    }
}
