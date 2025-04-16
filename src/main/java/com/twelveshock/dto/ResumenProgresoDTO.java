package com.twelveshock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumenProgresoDTO {
    private int totalTareas;
    private int tareasCompletadas;
    private double porcentajeCompletado;
}