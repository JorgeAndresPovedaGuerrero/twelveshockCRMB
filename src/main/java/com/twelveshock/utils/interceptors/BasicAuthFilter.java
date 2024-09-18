package com.twelveshock.utils.interceptors;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class BasicAuthFilter implements ClientRequestFilter {

    @ConfigProperty(name = "quarkus.rest-client.WoocommerceClient.authentication.username")
    String username;

    @ConfigProperty(name = "quarkus.rest-client.WoocommerceClient.authentication.password")
    String password;

    @Override
    public void filter(ClientRequestContext requestContext) {
        String token = username + ":" + password;
        String base64Token = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
        requestContext.getHeaders().add("Authorization", "Basic " + base64Token);
    }
}
