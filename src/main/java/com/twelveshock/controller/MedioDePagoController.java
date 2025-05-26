package com.twelveshock.controller;

import com.twelveshock.dao.entity.MedioDePago;
import com.twelveshock.dto.MedioDePagoDTO;
import com.twelveshock.repository.MedioDePagoRepository;
import com.twelveshock.service.impl.MedioDePagoService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/medios-pago")
@RolesAllowed("Admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedioDePagoController {

    @Inject
    MedioDePagoService medioDePagoService;

    @Inject
    MedioDePagoRepository medioDePagoRepository;

    @GET
    public List<MedioDePago> obtenerMediosDePago() {
        return medioDePagoService.obtenerMedios();
    }

    @POST
    public Response guardarMedioDePago(MedioDePago medio) {
        MedioDePago nuevoMedio = medioDePagoService.guardarMedio(medio);
        return Response.status(Response.Status.CREATED).entity(nuevoMedio).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarMedioDePago(@PathParam("id") String id, MedioDePago medioActualizado) {
        MedioDePago medio = medioDePagoService.actualizarMedio(id, medioActualizado);
        if (medio != null) {
            return Response.ok(medio).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarMedioDePago(@PathParam("id") String id) {
        boolean eliminado = medioDePagoService.eliminarMedio(id);
        if (eliminado) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/buscar")
    public Response buscarMediosDePago(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin) {

        List<MedioDePagoDTO> medios = medioDePagoRepository.buscarMediosDePago(fechaInicio, fechaFin);
        return Response.ok(medios).build();
    }
}