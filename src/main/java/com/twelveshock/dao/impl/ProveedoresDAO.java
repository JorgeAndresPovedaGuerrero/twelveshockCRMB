package com.twelveshock.dao.impl;

import com.twelveshock.dao.entity.Proveedor;
import com.twelveshock.dto.ProveedorDTO;
import io.quarkus.mongodb.panache.PanacheQuery;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProveedoresDAO {
    public List<ProveedorDTO> getProveedores(
            String fechaInicio,
            String fechaFin
    ) {

        // Consulta base sin filtros
        PanacheQuery<Proveedor> query = Proveedor.findAll();

        List<Proveedor> allProveedores = query.list();

        // Filtrar por fecha de inicio
        if (fechaInicio != null && !fechaInicio.isEmpty()) {
            LocalDate start = LocalDate.parse(fechaInicio);
            allProveedores = allProveedores.stream()
                    .filter(proveedor -> proveedor.getFechaCreacion().isEqual(start) || proveedor.getFechaCreacion().isAfter(start))
                    .collect(Collectors.toList());
        }

        // Filtrar por fecha de fin
        if (fechaFin != null && !fechaFin.isEmpty()) {
            LocalDate end = LocalDate.parse(fechaFin);
            allProveedores = allProveedores.stream()
                    .filter(proveedor -> proveedor.getFechaCreacion().isEqual(end) || proveedor.getFechaCreacion().isBefore(end))
                    .collect(Collectors.toList());
        }


        // Convertir a DTO
        return allProveedores.stream()
                .map(proveedor -> new ProveedorDTO(
                        proveedor.id.toString(),
                        proveedor.getFechaCreacion(),
                        proveedor.getCodigo(),
                        proveedor.getNombre()
                ))
                .collect(Collectors.toList());
    }
}
