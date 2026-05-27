package com.centrocomunitario.backend.repository;

import com.centrocomunitario.backend.model.UsuarioModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la colección 'usuarios'.
 * Prefijo 'I' → interface de repositorio.
 */
@Repository
public interface IUsuarios extends MongoRepository<UsuarioModel, String> {

    /** Buscar usuario por correo */
    Optional<UsuarioModel> findByCorreo(String correo);

    /** Buscar usuario por documento de identidad */
    Optional<UsuarioModel> findByDocumentoId(String documentoId);

    /** Listar usuarios por rol */
    List<UsuarioModel> findByRol(String rol);

    /** Listar usuarios por estado */
    List<UsuarioModel> findByEstado(String estado);

    /** Consulta nativa: buscar por nombre (insensible a mayúsculas) */
    @Query("{ 'nombre_completo': { $regex: ?0, $options: 'i' } }")
    List<UsuarioModel> buscarPorNombre(String nombre);

    /** Consulta nativa: usuarios activos por rol */
    @Query("{ 'rol': ?0, 'estado': 'activo' }")
    List<UsuarioModel> findActivosByRol(String rol);
}
