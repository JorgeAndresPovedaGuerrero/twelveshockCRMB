package com.twelveshock.dao.impl;

import com.twelveshock.dao.entity.Gasto;
import com.twelveshock.dto.GastoDTO;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GastosDAO {

    public List<GastoDTO> getGastos(
            String fechaInicio,
            String fechaFin,
            String concepto,
            Double precioMin,
            Double precioMax
    ) {

        // Consulta base sin filtros
        PanacheQuery<Gasto> query = Gasto.findAll();

        List<Gasto> allGastos = query.list();

        // Filtrar por fecha de inicio
        if (fechaInicio != null && !fechaInicio.isEmpty()) {
            LocalDate start = LocalDate.parse(fechaInicio);
            allGastos = allGastos.stream()
                    .filter(gasto -> gasto.getFecha().isEqual(start) || gasto.getFecha().isAfter(start))
                    .collect(Collectors.toList());
        }

        // Filtrar por fecha de fin
        if (fechaFin != null && !fechaFin.isEmpty()) {
            LocalDate end = LocalDate.parse(fechaFin);
            allGastos = allGastos.stream()
                    .filter(gasto -> gasto.getFecha().isEqual(end) || gasto.getFecha().isBefore(end))
                    .collect(Collectors.toList());
        }

        // Filtrar por concepto
        if (concepto != null && !concepto.isEmpty()) {
            allGastos = allGastos.stream()
                    .filter(gasto -> gasto.getConcepto().equalsIgnoreCase(concepto))
                    .collect(Collectors.toList());
        }

        // Filtrar por precio mínimo
        if (precioMin != null) {
            allGastos = allGastos.stream()
                    .filter(gasto -> gasto.getValor() >= precioMin)
                    .collect(Collectors.toList());
        }

        // Filtrar por precio máximo
        if (precioMax != null) {
            allGastos = allGastos.stream()
                    .filter(gasto -> gasto.getValor() <= precioMax)
                    .collect(Collectors.toList());
        }

        // Convertir a DTO
        return allGastos.stream()
                .map(gasto -> new GastoDTO(gasto.id.toString(), gasto.getFecha(), gasto.getValor(), gasto.getConcepto()))
                .collect(Collectors.toList());
    }
}