package com.twelveshock.controller;

import com.twelveshock.dao.entity.Producto;
import com.twelveshock.dto.ProductoDTO;
import com.twelveshock.repository.ProductoRepository;
import com.twelveshock.service.impl.ProductoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/productos")
@RolesAllowed("Admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductoController {

    @Inject
    ProductoService productoService;

    @Inject
    ProductoRepository productoRepository;

    @GET
    public List<Producto> obtenerProductos() {
        return productoService.obtenerProductos();
    }

    @POST
    public Response guardarProducto(Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return Response.status(Response.Status.CREATED).entity(nuevoProducto).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarProducto(@PathParam("id") String id, Producto productoActualizado) {
        Producto producto = productoService.actualizarProducto(id, productoActualizado);
        if (producto != null) {
            return Response.ok(producto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarProducto(@PathParam("id") String id) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/buscar")
    public Response buscarProductos(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        List<ProductoDTO> productos = productoRepository.buscarProductos(fechaInicio, fechaFin);
        return Response.ok(productos).build();
    }
}
