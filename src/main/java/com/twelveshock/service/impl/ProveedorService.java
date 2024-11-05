package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.Proveedor;
import com.twelveshock.repository.ProveedorRepository;
import com.twelveshock.service.contract.IProveedorService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class ProveedorService implements IProveedorService {

    @Inject
    ProveedorRepository proveedorRepository;

    public List<Proveedor> obtenerProveedores() {
        return proveedorRepository.listAll();
    }

    public Proveedor guardarProveedor(Proveedor proveedor) {
        proveedorRepository.persist(proveedor);
        return proveedor;
    }

    public Proveedor actualizarGasto(String id, Proveedor proveedorActualizado) {
        Proveedor proveedor = proveedorRepository.findById(new ObjectId(id));
        if (proveedor != null) {
            proveedor.setCodigo(proveedorActualizado.getCodigo());
            proveedor.setNombre(proveedorActualizado.getNombre());
            proveedor.setFechaCreacion(proveedorActualizado.getFechaCreacion());
            proveedorRepository.update(proveedor);
            return proveedor;
        }
        return null;
    }

    public boolean eliminarProveedor(String id) {
        return proveedorRepository.deleteById(new ObjectId(id));
    }
}
