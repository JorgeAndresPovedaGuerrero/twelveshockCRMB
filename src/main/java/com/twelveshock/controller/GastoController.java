package com.twelveshock.controller;

import com.twelveshock.dao.entity.Gasto;
import com.twelveshock.dto.GastoDTO;
import com.twelveshock.repository.GastoRepository;
import com.twelveshock.service.impl.GastoService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/gastos")
@RolesAllowed({"Admin"})
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GastoController {

    @Inject
    GastoService gastoService;

    @Inject
    GastoRepository gastoRepository;

    @Inject
    JsonWebToken jwt;

    @GET
    @RolesAllowed({"Admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public List<Gasto> obtenerGastos(@Context SecurityContext ctx) {
        String userName = ctx.getUserPrincipal() != null ? ctx.getUserPrincipal().getName() : "Unknown";
        System.out.println("Usuario actual: " + userName);
        // Lógica adicional según el usuario, si es necesario
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarGastos(
            @QueryParam("fechaInicio") String fechaInicio,
            @QueryParam("fechaFin") String fechaFin,
            @QueryParam("concepto") String concepto,
            @QueryParam("precioMin") Double precioMin,
            @QueryParam("precioMax") Double precioMax,
            @Context SecurityContext ctx) {

        // Verifica que el usuario autenticado coincide con el token JWT
        String responseString = getResponseString(ctx);

        // Realiza la búsqueda de gastos según los parámetros
        List<GastoDTO> gastos = gastoRepository.buscarGastos(fechaInicio, fechaFin, concepto, precioMin, precioMax);

        // Incluye la información del usuario en el log o en el retorno de la respuesta si deseas
        System.out.println("Información del usuario y JWT: " + responseString);

        // Retorna la lista de gastos como respuesta
        return Response.ok(gastos).build();
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) {
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) {
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName();
        }
        return String.format("hello %s,"
                        + " isHttps: %s,"
                        + " authScheme: %s,"
                        + " hasJWT: %s",
                name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt());
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}