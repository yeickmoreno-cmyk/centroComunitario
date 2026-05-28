package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.EvaluacionModel;
import com.centrocomunitario.backend.model.UsuarioModel;
import com.centrocomunitario.backend.repository.IEvaluaciones;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.repository.IUsuarios;
import com.centrocomunitario.backend.service.interfaces.IEvaluacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EvaluacionServiceImp implements IEvaluacionService {

    private final IEvaluaciones evaluacionRepository;
    private final IUsuarios usuarioRepository;
    private final IInscripciones inscripcionRepository;

    @Override
    public EvaluacionModel crear(EvaluacionModel evaluacion) {
        if (evaluacion.getValoracionNumerica() != null
                && (evaluacion.getValoracionNumerica() < 0 || evaluacion.getValoracionNumerica() > 5)) {
            throw new IllegalArgumentException("La valoración numérica debe estar entre 0 y 5");
        }

        // Verificar que el autor existe
        UsuarioModel autor = usuarioRepository.findById(evaluacion.getAutorId())
                .orElseThrow(() -> new NoSuchElementException(
                        "Usuario (autor) no encontrado con id: " + evaluacion.getAutorId()));

        // Si tipo = "evaluacion_participante"
        if ("evaluacion_participante".equals(evaluacion.getTipo())) {
            if (evaluacion.getActividadId() == null) {
                throw new IllegalArgumentException("Las evaluaciones de participante requieren actividadId");
            }
            boolean inscrito = inscripcionRepository
                    .findInscripcionActivaByUsuarioYReferencia(evaluacion.getAutorId(), evaluacion.getActividadId())
                    .isPresent();
            if (!inscrito) {
                throw new IllegalArgumentException("El participante no está inscrito en la actividad a evaluar");
            }
        }

        // Si tipo = "evaluacion_instructor"
        if ("evaluacion_instructor".equals(evaluacion.getTipo())) {
            if (!"instructor".equals(autor.getRol())) {
                throw new IllegalArgumentException(
                        "Solo los instructores pueden crear evaluaciones de tipo evaluacion_instructor");
            }
            if (evaluacion.getSujetoId() == null) {
                throw new IllegalArgumentException("Las evaluaciones de instructor requieren sujetoId");
            }
        }

        return evaluacionRepository.save(evaluacion);
    }

    @Override
    public List<EvaluacionModel> listarTodos() {
        return evaluacionRepository.findAll();
    }

    @Override
    public Optional<EvaluacionModel> buscarPorId(String id) {
        return evaluacionRepository.findById(id);
    }

    @Override
    public List<EvaluacionModel> buscarPorAutor(String autorId) {
        return evaluacionRepository.findByAutorId(autorId);
    }

    @Override
    public List<EvaluacionModel> buscarPorSujeto(String sujetoId) {
        return evaluacionRepository.findBySujetoId(sujetoId);
    }

    @Override
    public List<EvaluacionModel> buscarPorActividad(String actividadId) {
        return evaluacionRepository.findByActividadId(actividadId);
    }

    @Override
    public List<EvaluacionModel> buscarPorTipo(String tipo) {
        return evaluacionRepository.findByTipo(tipo);
    }

    @Override
    public EvaluacionModel actualizar(String id, EvaluacionModel evaluacion) {
        EvaluacionModel existente = evaluacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Evaluación no encontrada con id: " + id));

        existente.setValoracionNumerica(evaluacion.getValoracionNumerica());
        existente.setObservaciones(evaluacion.getObservaciones());
        existente.setSugerencias(evaluacion.getSugerencias());
        existente.setDesempenio(evaluacion.getDesempenio());
        existente.setCompromiso(evaluacion.getCompromiso());
        existente.setProgresoDescrito(evaluacion.getProgresoDescrito());
        existente.setFecha(evaluacion.getFecha());

        return evaluacionRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!evaluacionRepository.existsById(id)) {
            throw new NoSuchElementException("Evaluación no encontrada con id: " + id);
        }
        evaluacionRepository.deleteById(id);
    }
}
