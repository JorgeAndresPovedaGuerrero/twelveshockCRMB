package com.twelveshock.utils.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    UserService userService;

    @POST
    @Path("/signup")
    public Response signup(User user) {
        try {
            // Verificar si el usuario ya existe
            if (userService.findByUsername(user.getUsername()) != null) {
                return Response.status(Response.Status.CONFLICT)
                        .entity(new ErrorResponse("El usuario ya existe"))
                        .build();
            }

            // Establecer roles por defecto si no se proporcionan
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(Arrays.asList("User"));
            }

            // Guardar el usuario
            User savedUser = userService.createUser(user);
            return Response.status(Response.Status.CREATED)
                    .entity(savedUser)
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error al crear usuario: " + e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(UserCredentials credentials) {
        if (credentials == null || credentials.getUsername() == null || credentials.getPassword() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Credenciales incompletas"))
                    .build();
        }

        try {
            User user = userService.validateUser(credentials.getUsername(), credentials.getPassword());
            if (user == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ErrorResponse("Usuario o contraseña inválidos"))
                        .build();
            }

            if (!user.getRoles().contains("Admin") && !user.getRoles().contains("User")) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity(new ErrorResponse("Rol no autorizado"))
                        .build();
            }

            String token = Jwt.issuer("jwt-twelveshock")
                    .upn(user.getUsername())
                    .groups(new HashSet<>(user.getRoles()))
                    .claim(Claims.birthdate.name(), user.getBirthdate())
                    .expiresAt(Instant.now().plusSeconds(3600*4))
                    .sign();

            return Response.ok(new TokenResponse(token)).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error en el servidor: " + e.getMessage()))
                    .build();
        }
    }
}