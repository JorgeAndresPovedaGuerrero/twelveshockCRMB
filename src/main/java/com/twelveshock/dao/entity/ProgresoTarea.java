package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@MongoEntity(collection = "ProgresoTarea")
public class ProgresoTarea extends PanacheMongoEntity {
    @JsonProperty("fecha")
    private LocalDate fecha;

    // Map con key = id de tarea, value = estado (0=no completado, 1=completado, 2=no aplica)
    @JsonProperty("tareas")
    private Map<String, Integer> tareas;
}