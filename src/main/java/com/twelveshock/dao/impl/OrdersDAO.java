package com.twelveshock.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twelveshock.dao.contract.IOrdersDAO;
import com.twelveshock.dao.entity.Billing;
import com.twelveshock.dto.OrderDTO;
import com.twelveshock.dao.entity.OrderEntity;
import com.twelveshock.facade.WoocommerceClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrdersDAO implements IOrdersDAO {
    @Inject
    @RestClient
    WoocommerceClient woocommerceClient;

    @Override
    public List<OrderDTO> getOrders(
            String status,
            String startDate,
            String endDate
    ) {
        Response response = woocommerceClient.getOrders();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.registerModule(new JavaTimeModule());

        try {
            String responseBody = response.readEntity(String.class);
            List<OrderDTO> orders = objectMapper.readValue(responseBody, new TypeReference<List<OrderDTO>>() {});

            // Convertir las órdenes de WooCommerce a entidades de MongoDB y guardarlas
            List<OrderEntity> wooOrderEntities = orders.stream()
                    .map(orderDTO -> {
                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.id = orderDTO.getId();
                        orderEntity.status = orderDTO.getStatus();
                        orderEntity.currency = orderDTO.getCurrency();
                        orderEntity.dateCreated = orderDTO.getDate_created();
                        orderEntity.dateModified = orderDTO.getDate_modified();
                        orderEntity.total = orderDTO.getTotal();
                        orderEntity.totalTax = orderDTO.getTotal_tax();
                        orderEntity.billing = orderDTO.getBilling();
                        orderEntity.shipping = orderDTO.getShipping();
                        orderEntity.lineItems = orderDTO.getLine_items();
                        orderEntity.balance = orderDTO.getBalance();
                        orderEntity.dateBalance = orderDTO.getDate_balance();
                        orderEntity.meansOfPayment1 = orderEntity.getMeansOfPayment1();
                        orderEntity.meansOfPayment2 = orderEntity.getMeansOfPayment2();
                        orderEntity.downPayment = orderEntity.getDownPayment();
                        return orderEntity;
                    })
                    .collect(Collectors.toList());

            wooOrderEntities.forEach(orderEntity -> {
                OrderEntity existingOrder = OrderEntity.findById(orderEntity.id);
                if (existingOrder != null) {
                    existingOrder.status = orderEntity.status;
                    existingOrder.currency = orderEntity.currency;
                    existingOrder.dateCreated = orderEntity.dateCreated;
                    existingOrder.dateModified = orderEntity.dateModified;
                    existingOrder.total = orderEntity.total;
                    existingOrder.totalTax = orderEntity.totalTax;
                    existingOrder.billing = orderEntity.billing;
                    existingOrder.shipping = orderEntity.shipping;
                    existingOrder.lineItems = orderEntity.lineItems;
                    existingOrder.balance = orderEntity.balance;
                    existingOrder.dateBalance = orderEntity.dateBalance;
                    existingOrder.meansOfPayment1 = orderEntity.meansOfPayment1;
                    existingOrder.meansOfPayment2 = orderEntity.meansOfPayment2;
                    existingOrder.downPayment = orderEntity.downPayment;
                    existingOrder.update();
                } else {
                    orderEntity.persist();
                }
            });

            // Aplicar filtros en MongoDB
            List<OrderEntity> allOrders = OrderEntity.listAll();

            // Filtrar por estado
            if (status != null && !status.isEmpty()) {
                allOrders = allOrders.stream()
                        .filter(order -> status.equalsIgnoreCase(order.status))
                        .collect(Collectors.toList());
            }

            // Filtrar por fechas
            if (startDate != null && !startDate.isEmpty()) {
                LocalDateTime start = LocalDateTime.parse(startDate+"T00:00:00");
                allOrders = allOrders.stream()
                        .filter(order -> order.dateCreated.isAfter(start) || order.dateCreated.isEqual(start))
                        .collect(Collectors.toList());
            }

            if (endDate != null && !endDate.isEmpty()) {
                LocalDateTime end = LocalDateTime.parse(endDate+"T23:59:59");
                allOrders = allOrders.stream()
                        .filter(order -> order.dateCreated.isBefore(end) || order.dateCreated.isEqual(end))
                        .collect(Collectors.toList());
            }

            return allOrders.stream()
                    .map(orderEntity -> objectMapper.convertValue(orderEntity, OrderDTO.class))
                    .collect(Collectors.toList());

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public OrderDTO createOrder(OrderDTO orderDTO) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        orderDTO.setDate_created(LocalDateTime.now());

        OrderEntity orderEntity = objectMapper.convertValue(orderDTO, OrderEntity.class);
        orderEntity.persist();
        return objectMapper.convertValue(orderEntity, OrderDTO.class);
    }

    public OrderDTO updateOrder(long id, OrderDTO orderDTO) {
        // Obtén la entidad existente
        OrderEntity existingOrder = OrderEntity.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        // Actualiza los campos
        existingOrder.status = orderDTO.getStatus();
        existingOrder.currency = orderDTO.getCurrency();
        existingOrder.dateCreated = orderDTO.getDate_created();
        existingOrder.dateModified = orderDTO.getDate_modified();
        existingOrder.total = orderDTO.getTotal();
        existingOrder.totalTax = orderDTO.getTotal_tax();
        existingOrder.billing = orderDTO.getBilling();
        existingOrder.shipping = orderDTO.getShipping();
        existingOrder.lineItems = orderDTO.getLine_items();
        existingOrder.balance = orderDTO.getBalance();
        existingOrder.dateBalance = orderDTO.getDate_balance();
        existingOrder.downPayment = orderDTO.getDown_payment();
        existingOrder.meansOfPayment1 = orderDTO.getMeans_of_payment_1();
        existingOrder.meansOfPayment2 = orderDTO.getMeans_of_payment_2();

        // Guarda los cambios
        existingOrder.persistOrUpdate();

        // Convierte a DTO y devuelve
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.convertValue(existingOrder, OrderDTO.class);
    }

    public void deleteOrder(long id) {
        OrderEntity existingOrder = OrderEntity.findById(id);
        if (existingOrder != null) {
            existingOrder.delete();
        } else {
            throw new RuntimeException("Order not found with id: " + id);
        }
    }

    public long getHighestOrderId() {
        List<OrderEntity> allOrders = OrderEntity.listAll();
        return allOrders.stream()
                .mapToLong(OrderEntity::getId)
                .max()
                .orElse(0);
    }

    public long getHighestClientId() {
        List<OrderEntity> allOrders = OrderEntity.listAll();
        return allOrders.stream()
                .map(OrderEntity::getBilling)
                .filter(Objects::nonNull)
                .map(Billing::getIdCliente)
                .filter(Objects::nonNull)
                .mapToLong(idCliente -> idCliente)
                .max()
                .orElse(0);
    }

    public Billing findBillingByClientId(long idCliente) {
        try {
            List<OrderEntity> allOrders = OrderEntity.listAll();
            return allOrders.stream()
                    .map(OrderEntity::getBilling)
                    .filter(billing -> billing != null && billing.getIdCliente() != null && idCliente == billing.getIdCliente())
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar Billing por idCliente: " + idCliente, e); // Puedes lanzar una excepción específica si prefieres
        }
    }


}

