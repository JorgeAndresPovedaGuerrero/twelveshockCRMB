package com.twelveshock.facade;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "WoocommerceClient")
public interface WoocommerceClient {

    @GET
    @Path("/orders")
    @Produces(MediaType.APPLICATION_JSON)
    Response getOrders();

    @GET
    @Path("/products")
    @Produces(MediaType.APPLICATION_JSON)
    Response getProducts();
}