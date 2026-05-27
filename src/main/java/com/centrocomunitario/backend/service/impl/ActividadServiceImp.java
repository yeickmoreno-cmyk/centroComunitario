package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.model.ActividadModel;
import com.centrocomunitario.backend.repository.IActividades;
import com.centrocomunitario.backend.service.interfaces.IActividadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActividadServiceImp implements IActividadService {

    private final IActividades actividadRepository;

    @Override
    public ActividadModel crear(ActividadModel actividad) {
        if (actividad.getFechaFin().isBefore(actividad.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        if (actividad.getCuposDisponibles() > actividad.getCupoMaximo()) {
            throw new IllegalArgumentException("Los cupos disponibles no pueden superar el cupo máximo");
        }
        return actividadRepository.save(actividad);
    }

    @Override
    public List<ActividadModel> listarTodos() {
        return actividadRepository.findAll();
    }

    @Override
    public Optional<ActividadModel> buscarPorId(String id) {
        return actividadRepository.findById(id);
    }

    @Override
    public List<ActividadModel> buscarPorCategoria(String categoria) {
        return actividadRepository.findByCategoria(categoria);
    }

    @Override
    public List<ActividadModel> buscarPorEstado(String estado) {
        return actividadRepository.findByEstado(estado);
    }

    @Override
    public List<ActividadModel> buscarPorNombre(String nombre) {
        return actividadRepository.buscarPorNombre(nombre);
    }

    @Override
    public List<ActividadModel> listarConCuposDisponibles() {
        return actividadRepository.findConCuposDisponibles();
    }

    @Override
    public ActividadModel actualizar(String id, ActividadModel actividad) {
        ActividadModel existente = actividadRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Actividad no encontrada con id: " + id));

        existente.setNombre(actividad.getNombre());
        existente.setCategoria(actividad.getCategoria());
        existente.setDescripcion(actividad.getDescripcion());
        existente.setObjetivo(actividad.getObjetivo());
        existente.setFechaInicio(actividad.getFechaInicio());
        existente.setFechaFin(actividad.getFechaFin());
        existente.setIntensidadHoras(actividad.getIntensidadHoras());
        existente.setCupoMaximo(actividad.getCupoMaximo());
        existente.setCuposDisponibles(actividad.getCuposDisponibles());
        existente.setEstado(actividad.getEstado());
        existente.setRecursosRequeridos(actividad.getRecursosRequeridos());

        return actividadRepository.save(existente);
    }

    @Override
    public void eliminar(String id) {
        if (!actividadRepository.existsById(id)) {
            throw new NoSuchElementException("Actividad no encontrada con id: " + id);
        }
        actividadRepository.deleteById(id);
    }
}
