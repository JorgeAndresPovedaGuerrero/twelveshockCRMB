package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.Producto;
import com.twelveshock.repository.ProductoRepository;
import com.twelveshock.service.contract.IProductoService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class ProductoService implements IProductoService {

    @Inject
    ProductoRepository productoRepository;

    public List<Producto> obtenerProductos() {
        return productoRepository.listAll();
    }

    public Producto guardarProducto(Producto producto) {
        productoRepository.persist(producto);
        return producto;
    }

    public Producto actualizarProducto(String id, Producto productoActualizado) {
        Producto producto = productoRepository.findById(new ObjectId(id));
        if (producto != null) {
            producto.setCodigo(productoActualizado.getCodigo());
            producto.setNombre(productoActualizado.getNombre());
            producto.setFechaCreacion(productoActualizado.getFechaCreacion());
            productoRepository.update(producto);
        }
        return producto;
    }

    public boolean eliminarProducto(String id) {
        Producto producto = productoRepository.findById(new ObjectId(id));
        if (producto != null) {
            productoRepository.delete(producto);
            return true;
        }
        return false;
    }
}