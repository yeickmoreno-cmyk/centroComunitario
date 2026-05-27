package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.AnuncioForoModel;
import com.centrocomunitario.backend.model.Comentario;
import com.centrocomunitario.backend.repository.IAnunciosForos;
import com.centrocomunitario.backend.service.interfaces.IAnuncioForoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnuncioForoServiceImp implements IAnuncioForoService {

    private final IAnunciosForos anuncioForoRepository;

    @Override
    public AnuncioForoModel crear(AnuncioForoModel anuncio) {
        return anuncioForoRepository.save(anuncio);
    }

    @Override
    public List<AnuncioForoModel> listarTodos() {
        return anuncioForoRepository.findAll();
    }

    @Override
    public Optional<AnuncioForoModel> buscarPorId(String id) {
        return anuncioForoRepository.findById(id);
    }

    @Override
    public List<AnuncioForoModel> buscarPorTipo(String tipo) {
        return anuncioForoRepository.findByTipo(tipo);
    }

    @Override
    public List<AnuncioForoModel> buscarPorAutor(String autorId) {
        return anuncioForoRepository.findByAutorId(autorId);
    }

    @Override
    public List<AnuncioForoModel> buscarPorActividad(String actividadId) {
        return anuncioForoRepository.findByActividadId(actividadId);
    }

    @Override
    public List<AnuncioForoModel> buscarPorTitulo(String titulo) {
        return anuncioForoRepository.buscarPorTitulo(titulo);
    }

    @Override
    public List<Comentario> obtenerComentarios(String anuncioId) {
        AnuncioForoModel anuncio = anuncioForoRepository.findById(anuncioId)
                .orElseThrow(() -> new NoSuchElementException("Anuncio/Foro no encontrado con id: " + anuncioId));
        return anuncio.getComentarios() != null ? anuncio.getComentarios() : new ArrayList<>();
    }

    @Override
    public AnuncioForoModel agregarComentario(String anuncioId, Comentario comentario) {
        AnuncioForoModel anuncio = anuncioForoRepository.findById(anuncioId)
                .orElseThrow(() -> new NoSuchElementException("Anuncio/Foro no encontrado con id: " + anuncioId));

        List<Comentario> comentarios = anuncio.getComentarios();
        if (comentarios == null) {
            comentarios = new ArrayList<>();
        }
        comentarios.add(comentario);
        anuncio.setComentarios(comentarios);

        return anuncioForoRepository.save(anuncio);
    }

    @Override
    public AnuncioForoModel actualizar(String id, AnuncioForoModel anuncio) {
        AnuncioForoModel existente = anuncioForoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Anuncio/Foro no encontrado con id: " + id));

        existente.setTipo(anuncio.getTipo());
        existente.setTitulo(anuncio.getTitulo());
        existente.setContenido(anuncio.getContenido());
        existente.setEstado(anuncio.getEstado());
        existente.setAdjuntos(anuncio.getAdjuntos());
        existente.setActividadId(anuncio.getActividadId());
        existente.setProgramaId(anuncio.getProgramaId());

        return anuncioForoRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!anuncioForoRepository.existsById(id)) {
            throw new NoSuchElementException("Anuncio/Foro no encontrado con id: " + id);
        }
        anuncioForoRepository.deleteById(id);
    }
}
