package com.twelveshock.security.jwt;

import io.smallrye.jwt.build.Jwt;
import org.eclipse.microprofile.jwt.Claims;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;

public class GenerateToken {
    /**
     * Generate JWT token
     */
    public static void main(String[] args) {
        long expirationTime = 3600*4;
        String token =
                Jwt.issuer("jwt-twelveshock")
                        .upn("jdoe@quarkus.io")
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .claim(Claims.birthdate.name(), "2001-07-13")
                        .expiresAt(Instant.now().plusSeconds(expirationTime))
                        .sign();
        System.out.println("================token============");
        System.out.println(token);
        System.out.println("================token============");
    }
}
