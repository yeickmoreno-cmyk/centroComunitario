package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.UsuarioModel;

import java.util.List;
import java.util.Optional;

/**
 * Contrato de negocio para la entidad Usuario.
 */
public interface IUsuarioService {

    UsuarioModel crear(UsuarioModel usuario);

    List<UsuarioModel> listarTodos();

    Optional<UsuarioModel> buscarPorId(String id);

    Optional<UsuarioModel> buscarPorCorreo(String correo);

    List<UsuarioModel> buscarPorRol(String rol);

    List<UsuarioModel> buscarPorNombre(String nombre);

    UsuarioModel actualizar(String id, UsuarioModel usuario);

    void eliminar(String id);
}
