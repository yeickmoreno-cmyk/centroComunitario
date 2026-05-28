package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.model.ProgramaModel;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IActividades;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.IProgramas;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IProgramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramaServiceImp implements IProgramaService {

    private final IProgramas programaRepository;
    private final IInscripciones inscripcionRepository;
    private final IUsuarios usuarioRepository;
    private final IActividades actividadRepository;

    @Override
    public ProgramaModel crear(ProgramaModel programa) {
        if (programa.getFechaFin().isBefore(programa.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }

        // Verificar que cada responsable existe y tiene rol coordinador o administrador
        if (programa.getResponsablesId() != null) {
            for (String responsableId : programa.getResponsablesId()) {
                UsuarioModel responsable = usuarioRepository.findById(responsableId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "El responsable " + responsableId + " no existe o no tiene rol de coordinador/administrador"));
                if (!"coordinador".equals(responsable.getRol()) && !"administrador".equals(responsable.getRol())) {
                    throw new IllegalArgumentException(
                            "El responsable " + responsableId + " no existe o no tiene rol de coordinador/administrador");
                }
            }
        }

        // Verificar que cada actividad existe y no está cancelada
        if (programa.getActividadesId() != null) {
            for (String actividadId : programa.getActividadesId()) {
                ActividadModel actividad = actividadRepository.findById(actividadId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "La actividad " + actividadId + " no existe o está cancelada"));
                if ("cancelada".equals(actividad.getEstado())) {
                    throw new IllegalArgumentException(
                            "La actividad " + actividadId + " no existe o está cancelada");
                }
            }
        }

        return programaRepository.save(programa);
    }

    @Override
    public List<ProgramaModel> listarTodos() {
        return programaRepository.findAll();
    }

    @Override
    public Optional<ProgramaModel> buscarPorId(String id) {
        return programaRepository.findById(id);
    }

    @Override
    public List<ProgramaModel> buscarPorEstado(String estado) {
        return programaRepository.findByEstado(estado);
    }

    @Override
    public List<ProgramaModel> buscarPorNombre(String nombre) {
        return programaRepository.buscarPorNombre(nombre);
    }

    @Override
    public List<ProgramaModel> buscarPorResponsable(String responsableId) {
        return programaRepository.findByResponsablesIdContaining(responsableId);
    }

    @Override
    public ProgramaModel actualizar(String id, ProgramaModel programa) {
        ProgramaModel existente = programaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Programa no encontrado con id: " + id));

        String estadoAnterior = existente.getEstado();

        existente.setNombre(programa.getNombre());
        existente.setDescripcion(programa.getDescripcion());
        existente.setFechaInicio(programa.getFechaInicio());
        existente.setFechaFin(programa.getFechaFin());
        existente.setPoblacionObjetivo(programa.getPoblacionObjetivo());
        existente.setEstado(programa.getEstado());
        existente.setResponsablesId(programa.getResponsablesId());
        existente.setActividadesId(programa.getActividadesId());

        if (existente.getResponsablesId() != null) {
            for (String responsableId : existente.getResponsablesId()) {
                UsuarioModel responsable = usuarioRepository.findById(responsableId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "El responsable " + responsableId
                                + " no existe o no tiene rol de coordinador/administrador"));
                if (!"coordinador".equals(responsable.getRol())
                        && !"administrador".equals(responsable.getRol())) {
                    throw new IllegalArgumentException(
                            "El responsable " + responsableId
                            + " no existe o no tiene rol de coordinador/administrador");
                }
            }
        }

        if (existente.getActividadesId() != null) {
            for (String actividadId : existente.getActividadesId()) {
                ActividadModel actividad = actividadRepository.findById(actividadId)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "La actividad " + actividadId + " no existe o está cancelada"));
                if ("cancelada".equals(actividad.getEstado())) {
                    throw new IllegalArgumentException(
                            "La actividad " + actividadId + " no existe o está cancelada");
                }
            }
        }

        if (!"finalizado".equals(estadoAnterior) && "finalizado".equals(existente.getEstado())) {
            List<InscripcionModel> inscripciones = inscripcionRepository.findByReferenciaId(id);
            for (InscripcionModel inscripcion : inscripciones) {
                if ("programa".equals(inscripcion.getTipoInscripcion()) && "activa".equals(inscripcion.getEstado())) {
                    inscripcion.setEstado("cancelada");
                    inscripcion.setFechaCancelacion(LocalDate.now());
                    inscripcion.setMotivoCancelacion("Programa finalizado");
                    inscripcionRepository.save(inscripcion);
                }
            }
        }

        return programaRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!programaRepository.existsById(id)) {
            throw new NoSuchElementException("Programa no encontrado con id: " + id);
        }
        programaRepository.deleteById(id);
    }
}
