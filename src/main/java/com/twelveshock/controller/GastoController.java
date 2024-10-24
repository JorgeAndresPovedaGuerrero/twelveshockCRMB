package com.twelveshock.controller;

import com.twelveshock.dao.entity.Gasto;
import com.twelveshock.dto.GastoDTO;
import com.twelveshock.repository.GastoRepository;
import com.twelveshock.service.impl.GastoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/gastos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GastoController {

    @Inject
    GastoService gastoService;

    @Inject
    GastoRepository gastoRepository;

    @GET
    public List<Gasto> obtenerGastos() {
        return gastoService.obtenerGastos();
    }

    @POST
    public Response guardarGasto(Gasto gasto) {
        Gasto nuevoGasto = gastoService.guardarGasto(gasto);
        return Response.status(Response.Status.CREATED).entity(nuevoGasto).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarGasto(@PathParam("id") String id, Gasto gastoActualizado) {
        Gasto gasto = gastoService.actualizarGasto(id, gastoActualizado);
        if (gasto != null) {
            return Response.ok(gasto).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarGasto(@PathParam("id") String id) {
        boolean eliminado = gastoService.eliminarGasto(id);
        if (eliminado) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/buscar")
    public Response buscarGastos(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin,
            @QueryParam("concepto") String concepto,
            @QueryParam("precioMin") Double precioMin,
            @QueryParam("precioMax") Double precioMax) {

        List<GastoDTO> gastos = gastoRepository.buscarGastos(fechaInicio, fechaFin, concepto, precioMin, precioMax);
        return Response.ok(gastos).build();
    }
}