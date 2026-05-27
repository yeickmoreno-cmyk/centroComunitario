package com.centrocomunitario.backend.service.interfaces;

import com.centrocomunitario.backend.model.AnuncioForoModel;
import com.centrocomunitario.backend.model.Comentario;

import java.util.List;
import java.util.Optional;

public interface IAnuncioForoService {

    AnuncioForoModel crear(AnuncioForoModel anuncio);

    List<AnuncioForoModel> listarTodos();

    Optional<AnuncioForoModel> buscarPorId(String id);

    List<AnuncioForoModel> buscarPorTipo(String tipo);

    List<AnuncioForoModel> buscarPorAutor(String autorId);

    List<AnuncioForoModel> buscarPorActividad(String actividadId);

    List<AnuncioForoModel> buscarPorTitulo(String titulo);

    List<Comentario> obtenerComentarios(String anuncioId);

    AnuncioForoModel agregarComentario(String anuncioId, Comentario comentario);

    AnuncioForoModel actualizar(String id, AnuncioForoModel anuncio);

    void eliminar(String id);
}
