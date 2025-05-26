package com.twelveshock.repository;

import com.twelveshock.dao.entity.MedioDePago;
import com.twelveshock.dto.MedioDePagoDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MedioDePagoRepository implements PanacheMongoRepository<MedioDePago> {

    public List<MedioDePagoDTO> buscarMediosDePago(String fechaInicio, String fechaFin) {
        List<MedioDePago> medios;

        if (fechaInicio != null && fechaFin != null) {
            medios = list("fechaCreacion >= ?1 and fechaCreacion <= ?2", fechaInicio, fechaFin);
        } else {
            medios = listAll();
        }

        List<MedioDePagoDTO> resultado = new ArrayList<>();
        for (MedioDePago medio : medios) {
            MedioDePagoDTO dto = new MedioDePagoDTO();
            dto.setId(medio.id.toString());
            dto.setCodigo(medio.getCodigo());
            dto.setNombre(medio.getNombre());
            dto.setFechaCreacion(medio.getFechaCreacion());
            resultado.add(dto);
        }
        return resultado;
    }
}