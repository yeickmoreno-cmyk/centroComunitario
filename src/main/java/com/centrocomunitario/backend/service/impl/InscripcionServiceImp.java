package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.InscripcionModel;
import com.centrocomunitario.backend.repository.IInscripciones;
import com.centrocomunitario.backend.service.interfaces.IInscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImp implements IInscripcionService {

    private final IInscripciones inscripcionRepository;

    @Override
    public InscripcionModel crear(InscripcionModel inscripcion) {
        // Validar que no exista una inscripción activa duplicada
        inscripcionRepository
                .findInscripcionActivaByUsuarioYReferencia(inscripcion.getUsuarioId(), inscripcion.getReferenciaId())
                .ifPresent(i -> {
                    throw new IllegalArgumentException("El usuario ya tiene una inscripción activa en esta referencia");
                });
        return inscripcionRepository.save(inscripcion);
    }

    @Override
    public List<InscripcionModel> listarTodos() {
        return inscripcionRepository.findAll();
    }

    @Override
    public Optional<InscripcionModel> buscarPorId(String id) {
        return inscripcionRepository.findById(id);
    }

    @Override
    public List<InscripcionModel> buscarPorUsuario(String usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId);
    }

    @Override
    public List<InscripcionModel> buscarPorReferencia(String referenciaId) {
        return inscripcionRepository.findByReferenciaId(referenciaId);
    }

    @Override
    public List<InscripcionModel> buscarPorEstado(String estado) {
        return inscripcionRepository.findByEstado(estado);
    }

    @Override
    public InscripcionModel actualizar(String id, InscripcionModel inscripcion) {
        InscripcionModel existente = inscripcionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inscripción no encontrada con id: " + id));

        existente.setEstado(inscripcion.getEstado());
        existente.setProgreso(inscripcion.getProgreso());
        existente.setFechaCancelacion(inscripcion.getFechaCancelacion());
        existente.setMotivoCancelacion(inscripcion.getMotivoCancelacion());

        return inscripcionRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!inscripcionRepository.existsById(id)) {
            throw new NoSuchElementException("Inscripción no encontrada con id: " + id);
        }
        inscripcionRepository.deleteById(id);
    }
}
