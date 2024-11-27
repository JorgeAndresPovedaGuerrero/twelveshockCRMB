package com.twelveshock.utils.Schedulers;

import jakarta.enterprise.context.ApplicationScoped;
import io.quarkus.scheduler.Scheduled;

@ApplicationScoped
public class KeepAliveScheduler {

    @Scheduled(every = "10m") // Cada 5 minutos
    void pingSelf() {
        try {
            // Realiza un "ping" a tu propia aplicación
            java.net.HttpURLConnection connection =
                    (java.net.HttpURLConnection) new java.net.URL("https://twelveshockcrmb.onrender.com/health").openConnection();
                    //(java.net.HttpURLConnection) new java.net.URL("http://localhost:8080/health").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            System.out.println("Ping enviado, respuesta: " + connection.getResponseCode());
        } catch (Exception e) {
            System.err.println("Error en el ping: " + e.getMessage());
        }
    }
}
