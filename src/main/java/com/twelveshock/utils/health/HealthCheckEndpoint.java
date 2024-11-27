package com.twelveshock.utils.health;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/health")
public class HealthCheckEndpoint {
    @GET
    public String healthCheck() {
        return "OK";
    }
}
