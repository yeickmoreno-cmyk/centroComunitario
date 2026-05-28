package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.SesionModel;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IActividades;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.ISesiones;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.ISesionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SesionServiceImp implements ISesionService {

    private final ISesiones sesionRepository;
    private final IUsuarios usuarioRepository;
    private final IInscripciones inscripcionRepository;
    private final IActividades actividadRepository;

    @Override
    public SesionModel crear(SesionModel sesion) {
        // Verificar que la actividad existe
        ActividadModel actividad = actividadRepository.findById(sesion.getActividadId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Actividad no encontrada con id: " + sesion.getActividadId()));

        // Verificar que la actividad no está cancelada ni finalizada
        if ("cancelada".equals(actividad.getEstado()) || "finalizada".equals(actividad.getEstado())) {
            throw new IllegalArgumentException(
                    "No se puede crear una sesión para una actividad cancelada o finalizada");
        }

        // Verificar que hora_fin > hora_inicio (comparación lexicográfica válida para HH:mm)
        if (sesion.getHoraFin().compareTo(sesion.getHoraInicio()) <= 0) {
            throw new IllegalArgumentException("La hora de fin debe ser posterior a la hora de inicio");
        }

        // Verificar que no existe ya una sesión con el mismo numero_sesion para esa actividad
        boolean duplicado = sesionRepository
                .findByActividadIdOrderByNumeroSesionAsc(sesion.getActividadId())
                .stream()
                .anyMatch(s -> s.getNumeroSesion().equals(sesion.getNumeroSesion()));
        if (duplicado) {
            throw new IllegalArgumentException("Ya existe una sesión con ese número para esta actividad");
        }

        return sesionRepository.save(sesion);
    }

    @Override
    public List<SesionModel> listarTodos() {
        return sesionRepository.findAll();
    }

    @Override
    public Optional<SesionModel> buscarPorId(String id) {
        return sesionRepository.findById(id);
    }

    @Override
    public List<SesionModel> buscarPorActividad(String actividadId) {
        return sesionRepository.findByActividadIdOrderByNumeroSesionAsc(actividadId);
    }

    @Override
    public List<SesionModel> buscarPorEstado(String estado) {
        return sesionRepository.findByEstado(estado);
    }

    @Override
    public SesionModel actualizar(String id, SesionModel sesion) {
        SesionModel existente = sesionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Sesión no encontrada con id: " + id));

        existente.setActividadId(sesion.getActividadId());
        existente.setInstructorId(sesion.getInstructorId());
        existente.setNumeroSesion(sesion.getNumeroSesion());
        existente.setFecha(sesion.getFecha());
        existente.setHoraInicio(sesion.getHoraInicio());
        existente.setHoraFin(sesion.getHoraFin());
        existente.setModalidad(sesion.getModalidad());
        existente.setEspacioOEnlace(sesion.getEspacioOEnlace());
        existente.setObservaciones(sesion.getObservaciones());
        existente.setEstado(sesion.getEstado());
        existente.setAsistencia(sesion.getAsistencia());
        existente.setAdjuntos(sesion.getAdjuntos());

        return sesionRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!sesionRepository.existsById(id)) {
            throw new NoSuchElementException("Sesión no encontrada con id: " + id);
        }
        sesionRepository.deleteById(id);
    }

    @Override
    public SesionModel asignarInstructor(String sesionId, String instructorId) {
        SesionModel sesion = sesionRepository.findById(sesionId)
                .orElseThrow(() -> new NoSuchElementException("Sesión no encontrada con id: " + sesionId));

        UsuarioModel instructor = usuarioRepository.findById(instructorId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con id: " + instructorId));

        if (!"instructor".equals(instructor.getRol())) {
            throw new IllegalArgumentException(
                    "El usuario '" + instructor.getNombreCompleto() + "' no tiene rol de instructor");
        }

        sesion.setInstructorId(instructorId);
        return sesionRepository.save(sesion);
    }

    @Override
    public List<SesionModel> horarioParticipante(String usuarioId) {
        List<InscripcionModel> inscripciones = inscripcionRepository
                .findByUsuarioIdAndEstado(usuarioId, "activa");

        return inscripciones.stream()
                .filter(i -> "actividad".equals(i.getTipoInscripcion()))
                .flatMap(i -> sesionRepository
                        .findByActividadIdOrderByNumeroSesionAsc(i.getReferenciaId()).stream())
                .collect(Collectors.toList());
    }
}
