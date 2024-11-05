package com.twelveshock.repository;

import com.twelveshock.dao.entity.Proveedor;

import com.twelveshock.dao.impl.ProveedoresDAO;
import com.twelveshock.dto.ProveedorDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProveedorRepository implements PanacheMongoRepository<Proveedor> {
    @Inject
    ProveedoresDAO proveedoresDAO;

    public List<ProveedorDTO> buscarProveedores(String fechaInicio, String fechaFin) {
        return proveedoresDAO.getProveedores(fechaInicio, fechaFin);
    }
}
