package com.twelveshock.controller;

import com.twelveshock.dao.entity.Tarea;
import com.twelveshock.service.impl.TareaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/tareas")
@RolesAllowed("Admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TareaController {

    @Inject
    TareaService tareaService;

    @GET
    public List<Tarea> obtenerTareas() {
        return tareaService.obtenerTareas();
    }

    @GET
    @Path("/activas")
    public List<Tarea> obtenerTareasActivas() {
        return tareaService.obtenerTareasActivas();
    }

    @POST
    public Response guardarTarea(Tarea tarea) {
        Tarea nuevaTarea = tareaService.guardarTarea(tarea);
        return Response.status(Response.Status.CREATED).entity(nuevaTarea).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarTarea(@PathParam("id") String id, Tarea tareaActualizada) {
        Tarea tarea = tareaService.actualizarTarea(id, tareaActualizada);
        if (tarea != null) {
            return Response.ok(tarea).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarTarea(@PathParam("id") String id) {
        boolean eliminado = tareaService.eliminarTarea(id);
        if (eliminado) {
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}