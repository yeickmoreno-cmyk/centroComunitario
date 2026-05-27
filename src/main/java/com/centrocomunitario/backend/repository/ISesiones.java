package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.SesionModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio para la colección 'sesiones'.
 */
@Repository
public interface ISesiones extends MongoRepository<SesionModel, String> {

    /** Listar sesiones por actividad */
    List<SesionModel> findByActividadId(String actividadId);

    /** Listar sesiones por estado */
    List<SesionModel> findByEstado(String estado);

    /** Listar sesiones por modalidad */
    List<SesionModel> findByModalidad(String modalidad);

    /** Sesiones de una actividad ordenadas por número */
    List<SesionModel> findByActividadIdOrderByNumeroSesionAsc(String actividadId);

    /** Consulta nativa: sesiones de una fecha específica */
    @Query("{ 'fecha': ?0 }")
    List<SesionModel> findByFecha(LocalDate fecha);

    /** Consulta nativa: verificar si un usuario asistió a la sesión */
    @Query("{ '_id': ?0, 'asistencia': { $elemMatch: { 'usuario_id': ?1, 'asistio': true } } }")
    List<SesionModel> findSesionesDondeAsistioUsuario(String sesionId, String usuarioId);

    /** Consulta nativa: sesiones con asistencia pendiente de un usuario */
    @Query("{ 'actividad_id': ?0, 'asistencia.usuario_id': ?1 }")
    List<SesionModel> findSesionesByActividadYUsuario(String actividadId, String usuarioId);
}
