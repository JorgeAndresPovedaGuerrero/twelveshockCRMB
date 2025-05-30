package com.twelveshock.controller;

import com.twelveshock.dao.entity.VerificacionContraentrega;
import com.twelveshock.dto.OrderDTO;

import com.twelveshock.dto.VerificacionInputDTO;
import com.twelveshock.service.impl.ContraentregaService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Path("/contraentrega")
@RolesAllowed("Admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ContraentregaResource {

    @Inject
    ContraentregaService contraentregaService;

    @POST
    @Path("/")
    @Transactional
    public Response crearContraentrega(OrderDTO orderDTO) {
        try {
            VerificacionContraentrega resultado = contraentregaService.procesarContraentrega(orderDTO);

            if (resultado == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\": \"El método de pago no es contraentrega.\"}")
                        .build();
            }

            return Response.status(Response.Status.CREATED)
                    .entity(resultado)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al procesar la contraentrega: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @RolesAllowed({"Admin"})
    @Path("/")
    public Response listarContraentregas() {
        List<VerificacionContraentrega> lista = contraentregaService.obtenerTodas();
        return Response.ok(lista).build();
    }

    @GET
    @Path("/pendientes")
    public Response listarContraentregasPendientes() {
        List<VerificacionContraentrega> pendientes = contraentregaService.obtenerPendientes();
        return Response.ok(pendientes).build();
    }

    @PUT
    @Path("/{orderId}/verificar")
    @Transactional
    public Response verificarContraentrega(@PathParam("orderId") String orderId,
                                           @QueryParam("estado") String estado) {
        try {
            // Validar que el estado no sea null o vacío
            if (estado == null || estado.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El parámetro 'estado' es requerido"))
                        .build();
            }

            VerificacionContraentrega actualizada = contraentregaService.verificarTransaccion(orderId, estado);
            return Response.ok(actualizada).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error al verificar la transacción: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/estado/{estado}")
    public Response listarContraentregasPorEstado(@PathParam("estado") String estado) {
        try {
            List<VerificacionContraentrega> lista = contraentregaService.obtenerPorEstado(estado);
            return Response.ok(lista).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error al obtener contraentregas por estado: " + e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/ciudad/{ciudad}")
    public Response listarContraentregasPorCiudad(@PathParam("ciudad") String ciudad) {
        try {
            if (ciudad == null || ciudad.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(Map.of("error", "El parámetro 'ciudad' es requerido"))
                        .build();
            }

            List<VerificacionContraentrega> lista = contraentregaService.obtenerPorCiudad(ciudad.trim());
            return Response.ok(lista).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", "Error al obtener contraentregas por ciudad: " + e.getMessage()))
                    .build();
        }
    }
}