package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.ProgramaModel;

import java.util.List;
import java.util.Optional;

public interface IProgramaService {

    ProgramaModel crear(ProgramaModel programa);

    List<ProgramaModel> listarTodos();

    Optional<ProgramaModel> buscarPorId(String id);

    List<ProgramaModel> buscarPorEstado(String estado);

    List<ProgramaModel> buscarPorNombre(String nombre);

    List<ProgramaModel> buscarPorResponsable(String responsableId);

    ProgramaModel actualizar(String id, ProgramaModel programa);

    void eliminar(String id);
}
