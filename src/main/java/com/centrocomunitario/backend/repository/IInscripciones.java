package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.InscripcionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la colección 'inscripciones'.
 */
@Repository
public interface IInscripciones extends MongoRepository<InscripcionModel, String> {

    /** Listar inscripciones de un usuario */
    List<InscripcionModel> findByUsuarioId(String usuarioId);

    /** Listar inscripciones por referencia (actividad o programa) */
    List<InscripcionModel> findByReferenciaId(String referenciaId);

    /** Listar inscripciones por estado */
    List<InscripcionModel> findByEstado(String estado);

    /** Inscripciones activas de un usuario */
    List<InscripcionModel> findByUsuarioIdAndEstado(String usuarioId, String estado);

    /** Consulta nativa: inscripciones activas por tipo */
    @Query("{ 'tipo_inscripcion': ?0, 'estado': 'activa' }")
    List<InscripcionModel> findActivasByTipo(String tipoInscripcion);

    /** Consulta nativa: verificar si un usuario ya está inscrito en una referencia */
    @Query("{ 'usuario_id': ?0, 'referencia_id': ?1, 'estado': { $ne: 'cancelada' } }")
    Optional<InscripcionModel> findInscripcionActivaByUsuarioYReferencia(String usuarioId, String referenciaId);

    /** Contar inscritos activos por referencia */
    @Query(value = "{ 'referencia_id': ?0, 'estado': 'activa' }", count = true)
    long countActivasByReferencia(String referenciaId);
}
