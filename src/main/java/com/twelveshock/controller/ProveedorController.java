package com.twelveshock.controller;

import com.twelveshock.dao.entity.Proveedor;
import com.twelveshock.dto.GastoDTO;
import com.twelveshock.dto.ProveedorDTO;
import com.twelveshock.repository.ProveedorRepository;
import com.twelveshock.service.impl.ProveedorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/proveedor")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProveedorController {

    @Inject
    ProveedorService proveedorService;

    @Inject
    ProveedorRepository proveedorRepository;

    @GET
    public List<Proveedor> obtenerProveedores() {
        return proveedorService.obtenerProveedores();
    }

    @POST
    public Response guardarProveedor(Proveedor proveedor) {
        Proveedor nuevoProveedor = proveedorService.guardarProveedor(proveedor);
        return Response.status(Response.Status.CREATED).entity(nuevoProveedor).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarProveedor(@PathParam("id") String id, Proveedor proveedorActualizado) {
        Proveedor proveedor = proveedorService.actualizarGasto(id, proveedorActualizado);
        if (proveedor != null) {
            return Response.ok(proveedor).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarProveedor(@PathParam("id") String id) {
        boolean eliminado = proveedorService.eliminarProveedor(id);
        if (eliminado) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/buscar")
    public Response buscarProveedores(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        List<ProveedorDTO> proveedores = proveedorRepository.buscarProveedores(fechaInicio, fechaFin);
        return Response.ok(proveedores).build();
    }

}