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

    /** Asigna un instructor (rol='instructor') a una sesión */
    SesionModel asignarInstructor(String sesionId, String instructorId);

    /** Devuelve las sesiones de las actividades en las que el usuario está inscrito activamente */
    List<SesionModel> horarioParticipante(String usuarioId);
}
