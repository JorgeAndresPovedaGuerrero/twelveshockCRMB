package com.twelveshock.controller;

import com.twelveshock.dao.entity.Billing;
import com.twelveshock.dao.entity.LineItem;
import com.twelveshock.dao.entity.OrderEntity;
import com.twelveshock.dto.OrderDTO;
import com.twelveshock.facade.WoocommerceClient;
import com.twelveshock.service.impl.WooCommerceServiceImpl;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/data")
@RegisterForReflection(targets = {OrderDTO.class, LineItem.class, Billing.class, OrderEntity.class, OrderEntity.class,
                    })
public class WoocommerceResource {

    @Inject
    @RestClient
    WoocommerceClient woocommerceClient;

    @Inject
    WooCommerceServiceImpl wooCommerceService;

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    public List<OrderDTO> getOrders(
            @QueryParam("status") String status,
            @QueryParam("startDate") String startDate,
            @QueryParam("endDate") String endDate
    ) {
        return wooCommerceService.getOrders(status, startDate, endDate);
    }

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        return woocommerceClient.getProducts();
    }

    @POST
    @Path("/orders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(OrderDTO orderDTO) {
        System.out.println("Received OrderDTO: " + orderDTO);  // Log for debugging
        OrderDTO createdOrder = wooCommerceService.createOrder(orderDTO);
        return Response.ok(createdOrder).build();
    }

    @PUT
    @Path("/orders/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateOrder(@PathParam("id") long id, OrderDTO orderDTO) {
        try {
            OrderDTO updatedOrder = wooCommerceService.updateOrder(id, orderDTO);
            return Response.ok(updatedOrder).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/orders/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOrderById(@PathParam("id") long id) {
        try {
            OrderDTO orderDTO = wooCommerceService.getOrderById(id);
            return Response.ok(orderDTO).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/orders/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteOrder(@PathParam("id") long id) {
        try {
            wooCommerceService.deleteOrder(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("orders/highest-id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHighestOrderId() {
        long highestOrderId = wooCommerceService.getHighestOrderId();
        return Response.ok(highestOrderId).build();
    }

    @GET
    @Path("orders/highest-client-id")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHighestClientId() {
        try {
            long highestClientId = wooCommerceService.getHighestClientId();
            return Response.ok(highestClientId).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error retrieving highest client ID: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/orders/billing/{idCliente}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBillingByClientId(@PathParam("idCliente") long idCliente) {
        Billing billing = wooCommerceService.getBillingByClientId(idCliente);
        if (billing != null) {
            return Response.ok(billing).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Client ID not found").build();
        }
    }
}