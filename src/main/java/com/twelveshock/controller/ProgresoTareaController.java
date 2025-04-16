package com.twelveshock.controller;

import com.twelveshock.dao.entity.ProgresoTarea;
import com.twelveshock.dto.ProgresoTareaDTO;
import com.twelveshock.dto.ResumenProgresoDTO;
import com.twelveshock.service.impl.ProgresoTareaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;

@Path("/checklist")
@RolesAllowed("Admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProgresoTareaController {

    @Inject
    ProgresoTareaService progresoTareaService;

    @GET
    @Path("/progreso/{fecha}")
    public Response obtenerProgresoPorFecha(@PathParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            ProgresoTareaDTO progreso = progresoTareaService.obtenerProgresoPorFecha(fecha);

            // Si no existe progreso para la fecha indicada, inicializar uno nuevo
            if (progreso == null) {
                ProgresoTarea nuevoProgreso = progresoTareaService.inicializarProgresoDiario(fecha);
                progreso = new ProgresoTareaDTO(
                        nuevoProgreso.id.toString(),
                        nuevoProgreso.getFecha(),
                        nuevoProgreso.getTareas()
                );
            }

            return Response.ok(progreso).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Formato de fecha incorrecto").build();
        }
    }

    @GET
    @Path("/resumen/{fecha}")
    public Response obtenerResumenProgreso(@PathParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            ResumenProgresoDTO resumen = progresoTareaService.obtenerResumenProgreso(fecha);
            return Response.ok(resumen).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Formato de fecha incorrecto").build();
        }
    }

    @POST
    @Path("/inicializar/{fecha}")
    public Response inicializarProgresoDiario(@PathParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            ProgresoTarea progreso = progresoTareaService.inicializarProgresoDiario(fecha);
            return Response.status(Response.Status.CREATED).entity(progreso).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Formato de fecha incorrecto").build();
        }
    }

    @PUT
    @Path("/tarea/{idProgreso}/{idTarea}/{estado}")
    public Response actualizarEstadoTarea(
            @PathParam("idProgreso") String idProgreso,
            @PathParam("idTarea") String idTarea,
            @PathParam("estado") int estado) {

        boolean actualizado = progresoTareaService.actualizarEstadoTarea(idProgreso, idTarea, estado);
        if (actualizado) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
