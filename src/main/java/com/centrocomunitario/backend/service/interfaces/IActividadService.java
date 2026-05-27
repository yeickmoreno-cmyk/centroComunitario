package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.ActividadModel;

import java.util.List;
import java.util.Optional;

public interface IActividadService {

    ActividadModel crear(ActividadModel actividad);

    List<ActividadModel> listarTodos();

    Optional<ActividadModel> buscarPorId(String id);

    List<ActividadModel> buscarPorCategoria(String categoria);

    List<ActividadModel> buscarPorEstado(String estado);

    List<ActividadModel> buscarPorNombre(String nombre);

    List<ActividadModel> listarConCuposDisponibles();

    ActividadModel actualizar(String id, ActividadModel actividad);

    void eliminar(String id);
}
