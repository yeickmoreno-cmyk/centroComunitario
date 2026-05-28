package com.centrocomunitario.backend.service.impl;

import com.centrocomunitario.backend.dto.EstadisticasDto;
import com.centrocomunitario.backend.dto.PromedioValoracionDto;
import com.centrocomunitario.backend.service.interfaces.IEstadisticaService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Estadísticas generales del centro usando agregaciones de MongoDB.
 */
@Service
@RequiredArgsConstructor
public class EstadisticaServiceImp implements IEstadisticaService {

    private final MongoTemplate mongoTemplate;

    @Override
    public EstadisticasDto obtenerResumen() {
        return EstadisticasDto.builder()
                .usuariosPorRol(contarPorCampo("usuarios", "rol"))
                .actividadesPorEstado(contarPorCampo("actividades", "estado"))
                .inscripcionesPorEstado(contarPorCampo("inscripciones", "estado"))
                .promedioValoracionPorActividad(calcularPromedioValoracion())
                .build();
    }

    /**
     * Agrupa los documentos de una colección por el campo dado y cuenta cuántos hay en cada grupo.
     */
    private Map<String, Long> contarPorCampo(String coleccion, String campo) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.group(campo).count().as("total")
        );
        AggregationResults<Document> resultados = mongoTemplate.aggregate(agg, coleccion, Document.class);

        Map<String, Long> mapa = new LinkedHashMap<>();
        for (Document doc : resultados) {
            String clave = doc.getString("_id");
            Object totalObj = doc.get("total");
            long total = totalObj != null ? ((Number) totalObj).longValue() : 0L;
            if (clave != null) {
                mapa.put(clave, total);
            }
        }
        return mapa;
    }

    /**
     * Calcula el promedio de valoracion_numerica por actividad_id en la colección 'evaluaciones'.
     */
    private List<PromedioValoracionDto> calcularPromedioValoracion() {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria.where("actividad_id").exists(true).ne(null)
                                .and("valoracion_numerica").exists(true).ne(null)
                ),
                Aggregation.group("actividad_id").avg("valoracion_numerica").as("promedio")
        );
        AggregationResults<Document> resultados = mongoTemplate.aggregate(agg, "evaluaciones", Document.class);

        List<PromedioValoracionDto> lista = new ArrayList<>();
        for (Document doc : resultados) {
            String actividadId = doc.getString("_id");
            Object promedioObj = doc.get("promedio");
            double promedio = promedioObj != null ? ((Number) promedioObj).doubleValue() : 0.0;
            if (actividadId != null) {
                lista.add(new PromedioValoracionDto(actividadId, promedio));
            }
        }
        return lista;
    }
}
