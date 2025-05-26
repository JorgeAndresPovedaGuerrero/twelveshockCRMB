package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.Producto;

import java.util.List;

public interface IProductoService {
    List<Producto> obtenerProductos();
    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(String id, Producto productoActualizado);
    boolean eliminarProducto(String id);
}