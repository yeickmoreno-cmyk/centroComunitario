package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.InscripcionModel;

import java.util.List;
import java.util.Optional;

public interface IInscripcionService {

    InscripcionModel crear(InscripcionModel inscripcion);

    List<InscripcionModel> listarTodos();

    Optional<InscripcionModel> buscarPorId(String id);

    List<InscripcionModel> buscarPorUsuario(String usuarioId);

    List<InscripcionModel> buscarPorReferencia(String referenciaId);

    List<InscripcionModel> buscarPorEstado(String estado);

    InscripcionModel actualizar(String id, InscripcionModel inscripcion);

    void eliminar(String id);
}
