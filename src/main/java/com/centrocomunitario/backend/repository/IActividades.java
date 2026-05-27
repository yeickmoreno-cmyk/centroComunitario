package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.ActividadModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la colección 'actividades'.
 */
@Repository
public interface IActividades extends MongoRepository<ActividadModel, String> {

    /** Listar actividades por estado */
    List<ActividadModel> findByEstado(String estado);

    /** Listar actividades por categoría */
    List<ActividadModel> findByCategoria(String categoria);

    /** Listar actividades por quien las propuso */
    List<ActividadModel> findByPropuestoPor(String propuestoPorId);

    /** Consulta nativa: actividades con cupos disponibles */
    @Query("{ 'cupos_disponibles': { $gt: 0 }, 'estado': 'programada' }")
    List<ActividadModel> findConCuposDisponibles();

    /** Consulta nativa: buscar por nombre */
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    List<ActividadModel> buscarPorNombre(String nombre);

    /** Consulta nativa: actividades en curso o programadas */
    @Query("{ 'estado': { $in: ['en_curso', 'programada'] } }")
    List<ActividadModel> findActivasOProgramadas();
}
