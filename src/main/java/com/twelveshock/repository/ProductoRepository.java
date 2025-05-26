package com.twelveshock.repository;

import com.twelveshock.dao.entity.Producto;
import com.twelveshock.dto.ProductoDTO;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ProductoRepository implements PanacheMongoRepository<Producto> {

    public List<ProductoDTO> buscarProductos(String fechaInicio, String fechaFin) {
        List<Producto> productos;

        if (fechaInicio != null && fechaFin != null) {
            productos = list("fechaCreacion >= ?1 and fechaCreacion <= ?2", fechaInicio, fechaFin);
        } else {
            productos = listAll();
        }

        List<ProductoDTO> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(producto.id.toString());
            dto.setCodigo(producto.getCodigo());
            dto.setNombre(producto.getNombre());
            dto.setFechaCreacion(producto.getFechaCreacion());
            resultado.add(dto);
        }
        return resultado;
    }
}