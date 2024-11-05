package com.twelveshock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RegisterForReflection
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProveedorDTO {
    private String id;
    private LocalDate fechaCreacion;
    private String codigo;
    private String nombre;

    public ProveedorDTO(String id, LocalDate fechaCreacion) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
    }
}
