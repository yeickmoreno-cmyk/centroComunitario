package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.EvaluacionModel;

import java.util.List;
import java.util.Optional;

public interface IEvaluacionService {

    EvaluacionModel crear(EvaluacionModel evaluacion);

    List<EvaluacionModel> listarTodos();

    Optional<EvaluacionModel> buscarPorId(String id);

    List<EvaluacionModel> buscarPorAutor(String autorId);

    List<EvaluacionModel> buscarPorSujeto(String sujetoId);

    List<EvaluacionModel> buscarPorActividad(String actividadId);

    List<EvaluacionModel> buscarPorTipo(String tipo);

    EvaluacionModel actualizar(String id, EvaluacionModel evaluacion);

    void eliminar(String id);
}
