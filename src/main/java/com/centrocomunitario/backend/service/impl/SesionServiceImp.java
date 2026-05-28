package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.SesionModel;
import com.centrocomunitario.backend.model.UsuarioModel;
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

    @Override
    public SesionModel crear(SesionModel sesion) {
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
