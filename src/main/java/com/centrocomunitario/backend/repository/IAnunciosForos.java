package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.AnuncioForoModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la colección 'anuncios_foros'.
 */
@Repository
public interface IAnunciosForos extends MongoRepository<AnuncioForoModel, String> {

    /** Listar por tipo */
    List<AnuncioForoModel> findByTipo(String tipo);

    /** Listar por estado */
    List<AnuncioForoModel> findByEstado(String estado);

    /** Anuncios de un autor */
    List<AnuncioForoModel> findByAutorId(String autorId);

    /** Anuncios de una actividad */
    List<AnuncioForoModel> findByActividadId(String actividadId);

    /** Anuncios de un programa */
    List<AnuncioForoModel> findByProgramaId(String programaId);

    /** Consulta nativa: buscar por título */
    @Query("{ 'titulo': { $regex: ?0, $options: 'i' } }")
    List<AnuncioForoModel> buscarPorTitulo(String titulo);

    /** Consulta nativa: anuncios activos ordenados por fecha desc */
    @Query(value = "{ 'estado': 'activo' }", sort = "{ 'fecha_publicacion': -1 }")
    List<AnuncioForoModel> findAnunciosActivosRecientes();

    /** Consulta nativa: recuperar comentarios de un anuncio específico */
    @Query(value = "{ '_id': ?0 }", fields = "{ 'comentarios': 1 }")
    AnuncioForoModel findComentariosByAnuncioId(String anuncioId);
}
