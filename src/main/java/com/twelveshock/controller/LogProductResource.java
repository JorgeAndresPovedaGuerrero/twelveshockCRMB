package com.twelveshock.controller;

import com.twelveshock.dao.entity.LogProduct;
import com.twelveshock.service.contract.ILogProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/api/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogProductResource {

    @Inject
    ILogProductService logProductService;

    @GET
    @Path("/order/{orderId}")
    public Response getLogsByOrderId(@PathParam("orderId") long orderId) {
        try {
            List<LogProduct> logs = logProductService.getLogsByOrderId(orderId);
            if (logs.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No logs found for order ID: " + orderId)
                        .build();
            }
            return Response.ok(logs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving logs: " + e.getMessage())
                    .build();
        }
    }
}
