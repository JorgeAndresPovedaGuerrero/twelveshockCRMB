package com.twelveshock.repository;

import com.twelveshock.dao.entity.Gasto;
import com.twelveshock.dao.impl.GastosDAO;
import com.twelveshock.dto.GastoDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class GastoRepository implements PanacheMongoRepository<Gasto> {
    @Inject
    GastosDAO gastosDAO;

    public List<GastoDTO> buscarGastos(String fechaInicio, String fechaFin, String concepto, Double precioMin, Double precioMax) {
        return gastosDAO.getGastos(fechaInicio, fechaFin, concepto, precioMin, precioMax);
    }
}
