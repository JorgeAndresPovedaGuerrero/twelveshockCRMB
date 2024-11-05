package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@MongoEntity(collection = "Proveedor")
public class Proveedor extends PanacheMongoEntity {
    @JsonProperty("codigo")
    private String codigo;
    @JsonProperty("fechaCreacion")
    private LocalDate fechaCreacion;
    @JsonProperty("nombre")
    private String nombre;
}