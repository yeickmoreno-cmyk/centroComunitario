package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.ProgramaModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la colección 'programas'.
 */
@Repository
public interface IProgramas extends MongoRepository<ProgramaModel, String> {

    /** Programas por estado */
    List<ProgramaModel> findByEstado(String estado);

    /** Programas donde participa un responsable */
    List<ProgramaModel> findByResponsablesIdContaining(String responsableId);

    /** Programas que contienen una actividad */
    List<ProgramaModel> findByActividadesIdContaining(String actividadId);

    /** Consulta nativa: buscar por nombre */
    @Query("{ 'nombre': { $regex: ?0, $options: 'i' } }")
    List<ProgramaModel> buscarPorNombre(String nombre);

    /** Consulta nativa: programas activos con actividades */
    @Query("{ 'estado': { $ne: 'finalizado' }, 'actividades_id': { $exists: true, $not: { $size: 0 } } }")
    List<ProgramaModel> findProgramasActivosConActividades();
}
