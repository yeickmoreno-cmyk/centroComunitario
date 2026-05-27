package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.SesionModel;

import java.util.List;
import java.util.Optional;

public interface ISesionService {

    SesionModel crear(SesionModel sesion);

    List<SesionModel> listarTodos();

    Optional<SesionModel> buscarPorId(String id);

    List<SesionModel> buscarPorActividad(String actividadId);

    List<SesionModel> buscarPorEstado(String estado);

    SesionModel actualizar(String id, SesionModel sesion);

    void eliminar(String id);
}
