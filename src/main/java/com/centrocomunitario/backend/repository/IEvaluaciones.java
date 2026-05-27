package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.EvaluacionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la colección 'evaluaciones'.
 */
@Repository
public interface IEvaluaciones extends MongoRepository<EvaluacionModel, String> {

    /** Evaluaciones realizadas por un autor */
    List<EvaluacionModel> findByAutorId(String autorId);

    /** Evaluaciones recibidas por un sujeto */
    List<EvaluacionModel> findBySujetoId(String sujetoId);

    /** Evaluaciones de una actividad */
    List<EvaluacionModel> findByActividadId(String actividadId);

    /** Evaluaciones de una sesión */
    List<EvaluacionModel> findBySesionId(String sesionId);

    /** Evaluaciones por tipo */
    List<EvaluacionModel> findByTipo(String tipo);

    /** Consulta nativa: evaluaciones de un instructor en una actividad */
    @Query("{ 'tipo': 'evaluacion_instructor', 'sujeto_id': ?0, 'actividad_id': ?1 }")
    List<EvaluacionModel> findEvaluacionesInstructorEnActividad(String instructorId, String actividadId);

    /** Consulta nativa: evaluaciones de participante con valoración >= umbral */
    @Query("{ 'tipo': 'evaluacion_participante', 'actividad_id': ?0, 'valoracion_numerica': { $gte: ?1 } }")
    List<EvaluacionModel> findEvaluacionesConBuenaValoracion(String actividadId, Double umbral);
}
