package com.twelveshock.dao.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@MongoEntity(collection = "Gastos")
public class Gasto extends PanacheMongoEntity {

    @JsonProperty("fecha")
    @NotNull
    private LocalDate fecha;

    @JsonProperty("valor")
    @NotNull
    private Double valor;

    @JsonProperty("concepto")
    @NotNull
    private String concepto;
}
