package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.VerificacionContraentrega;
import com.twelveshock.dto.OrderDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.bson.types.ObjectId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ContraentregaService {

    private static final String CODIGO_CONTRAENTREGA = "Pago Contraentrega";

    @Transactional
    public VerificacionContraentrega procesarContraentrega(OrderDTO orderDTO) {
        if (!CODIGO_CONTRAENTREGA.equals(orderDTO.getMeans_of_payment_2())) {
            return null; // No es contraentrega, no hace nada
        }

        // Verificar si ya existe una verificación para esta orden
        VerificacionContraentrega existente = VerificacionContraentrega.find("orderId", orderDTO.getId()).firstResult();
        if (existente != null) {
            // Ya existe, podrías retornar la existente o lanzar excepción, según tu regla de negocio
            // Aquí retornamos la existente sin crear duplicado
            return existente;
        }

        VerificacionContraentrega verificacion = new VerificacionContraentrega();

        verificacion.orderId = orderDTO.getId();
        verificacion.ciudadEnvio = orderDTO.getShipping() != null ? orderDTO.getShipping().getCity() : null;
        verificacion.idCliente = orderDTO.getBilling() != null ? orderDTO.getBilling().getIdCliente() : null;
        verificacion.nombreCliente = orderDTO.getBilling() != null ?
                orderDTO.getBilling().getFirstName() + " " + orderDTO.getBilling().getLastName() : null;

        verificacion.saldo = new BigDecimal(orderDTO.getBalance() != null ? orderDTO.getBalance() : "0");

        if (orderDTO.getShipping() != null && orderDTO.getShipping().getPriceShipping() != null) {
            verificacion.precioEnvio = new BigDecimal(orderDTO.getShipping().getPriceShipping().toString());
        } else {
            verificacion.precioEnvio = BigDecimal.ZERO;
        }

        verificacion.total = verificacion.saldo.add(verificacion.precioEnvio);

        verificacion.estado = VerificacionContraentrega.EstadoContraentrega.PENDIENTE;
        verificacion.fechaCreacion = LocalDateTime.now();

        verificacion.persist();

        return verificacion;
    }

    public List<VerificacionContraentrega> obtenerTodas() {
        return VerificacionContraentrega.listAll();
    }

    public List<VerificacionContraentrega> obtenerPendientes() {
        return VerificacionContraentrega.find("estado", VerificacionContraentrega.EstadoContraentrega.PENDIENTE).list();
    }


    @Transactional
    public VerificacionContraentrega verificarTransaccion(String orderId, String nuevoEstado) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("El orderId no puede ser null o vacío");
        }

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser null o vacío");
        }

        Long orderIdLong;
        try {
            orderIdLong = Long.parseLong(orderId.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El orderId debe ser un número válido: " + orderId);
        }

        VerificacionContraentrega.EstadoContraentrega estado;
        try {
            estado = VerificacionContraentrega.EstadoContraentrega.valueOf(nuevoEstado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado + ". Estados válidos: " +
                    java.util.Arrays.toString(VerificacionContraentrega.EstadoContraentrega.values()));
        }

        VerificacionContraentrega verificacion = VerificacionContraentrega.find("orderId", orderIdLong).firstResult();

        if (verificacion == null) {
            throw new NotFoundException("Verificación no encontrada para la orden: " + orderId);
        }

        verificacion.estado = estado;
        verificacion.fechaVerificacion = LocalDateTime.now();
        verificacion.update();  // Actualiza el documento en la base de datos

        return verificacion;
    }

    public List<VerificacionContraentrega> obtenerPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede ser null o vacío");
        }

        VerificacionContraentrega.EstadoContraentrega estadoEnum;
        try {
            estadoEnum = VerificacionContraentrega.EstadoContraentrega.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado + ". Estados válidos: " +
                    java.util.Arrays.toString(VerificacionContraentrega.EstadoContraentrega.values()));
        }

        return VerificacionContraentrega.find("estado", estadoEnum).list();
    }

    public List<VerificacionContraentrega> obtenerPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad no puede ser null o vacía");
        }

        return VerificacionContraentrega.find(
                "{ ciudadEnvio: { $regex: ?1, $options: 'i' } }", ciudad
        ).list();
    }
}