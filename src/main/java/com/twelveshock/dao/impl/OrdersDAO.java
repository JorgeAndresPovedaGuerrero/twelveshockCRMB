package com.twelveshock.dao.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twelveshock.dao.contract.IOrdersDAO;
import com.twelveshock.dao.entity.*;
import com.twelveshock.dto.OrderDTO;
import com.twelveshock.facade.WoocommerceClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public OrderDTO createOrder(OrderDTO orderDTO, boolean isManual) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        orderDTO.setDate_created(LocalDateTime.now());

        OrderEntity orderEntity = objectMapper.convertValue(orderDTO, OrderEntity.class);
        // Si la orden es manual, sumamos 100000000 al ID
        if (isManual) {
            orderEntity.setId(orderEntity.getId() + 100000000);
        }
        orderEntity.persist();
        return objectMapper.convertValue(orderEntity, OrderDTO.class);
    }

    public OrderDTO updateOrder(long id, OrderDTO orderDTO) {
        OrderEntity existingOrder = OrderEntity.findById(id);
        if (existingOrder == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }

        List<String> changes = new ArrayList<>();

        // Verifica y registra cambios
        if (!existingOrder.status.equals(orderDTO.getStatus())) {
            changes.add("Se cambió de \"" + existingOrder.status + "\" a \"" + orderDTO.getStatus() + "\"");
        }

        // Verifica cambios en billing y shipping
        checkBillingChanges(existingOrder.billing, orderDTO.getBilling(), changes);
        checkShippingChanges(existingOrder.shipping, orderDTO.getShipping(), changes);

        // Verifica cambios en otros campos del pedido
        checkOrderChanges(existingOrder, orderDTO, changes);

        // Verifica cambios en los productos
        checkProductChanges(existingOrder.lineItems, orderDTO.getLine_items(), id);

        // Si hay cambios acumulados, regístralos
        if (!changes.isEmpty()) {
            logChange(id, "Actualización del pedido", changes);
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

    private void checkBillingChanges(Billing existingBilling, Billing newBilling, List<String> changes) {
        // Compara cada campo de Billing y agrega los cambios a la lista
        if (!existingBilling.getFirstName().equals(newBilling.getFirstName())) {
            changes.add("Nombre de facturación: " + existingBilling.getFirstName() + " -> " + newBilling.getFirstName());
        }
        if (!existingBilling.getAddress1().equals(newBilling.getAddress1())) {
            changes.add("Dirección de facturación: " + existingBilling.getAddress1() + " -> " + newBilling.getAddress1());
        }
        if (!existingBilling.getAddress2().equals(newBilling.getAddress2())) {
            changes.add("Complemento de Dirección de facturación: " + existingBilling.getAddress2() + " -> " + newBilling.getAddress2());
        }
        if (!existingBilling.getCity().equals(newBilling.getCity())) {
            changes.add("Ciudad de facturación: " + existingBilling.getCity() + " -> " + newBilling.getCity());
        }
        if (!existingBilling.getIdentification().equals(newBilling.getIdentification())) {
            changes.add("Cédula de facturación: " + existingBilling.getIdentification() + " -> " + newBilling.getIdentification());
        }
        if (!existingBilling.getCountry().equals(newBilling.getCountry())) {
            changes.add("País de facturación: " + existingBilling.getCountry() + " -> " + newBilling.getCountry());
        }
        if (!existingBilling.getEmail().equals(newBilling.getEmail())) {
            changes.add("Email de facturación: " + existingBilling.getEmail() + " -> " + newBilling.getEmail());
        }
        if (!existingBilling.getLastName().equals(newBilling.getLastName())) {
            changes.add("Apellido de facturación: " + existingBilling.getLastName() + " -> " + newBilling.getLastName());
        }
        if (!existingBilling.getPhone().equals(newBilling.getPhone())) {
            changes.add("Teléfono de facturación: " + existingBilling.getPhone() + " -> " + newBilling.getPhone());
        }
        if (!existingBilling.getPhone2().equals(newBilling.getPhone2())) {
            changes.add("Teléfono 2 de facturación: " + existingBilling.getPhone2() + " -> " + newBilling.getPhone2());
        }
        if (!existingBilling.getPostcode().equals(newBilling.getPostcode())) {
            changes.add("PostCode de facturación: " + existingBilling.getPostcode() + " -> " + newBilling.getPostcode());
        }
        if (!existingBilling.getState().equals(newBilling.getState())) {
            changes.add("Departamento de facturación: " + existingBilling.getState() + " -> " + newBilling.getState());
        }

    }

    private void checkShippingChanges(Shipping existingShipping, Shipping newShipping, List<String> changes) {
        // Compara cada campo de Shipping y agrega los cambios a la lista
        if (!existingShipping.getAddress1().equals(newShipping.getAddress1())) {
            changes.add("Dirección de envío: " + existingShipping.getAddress1() + " -> " + newShipping.getAddress1());
        }
        if (!existingShipping.getAddress2().equals(newShipping.getAddress2())) {
            changes.add("Complemento de dirección de envío: " + existingShipping.getAddress2() + " -> " + newShipping.getAddress2());
        }
        if (!existingShipping.getFirstName().equals(newShipping.getFirstName())) {
            changes.add("Nombre de envío: " + existingShipping.getFirstName() + " -> " + newShipping.getFirstName());
        }
        if (!existingShipping.getLastName().equals(newShipping.getLastName())) {
            changes.add("Apellido de envío: " + existingShipping.getLastName() + " -> " + newShipping.getLastName());
        }
        if (!existingShipping.getPostcode().equals(newShipping.getPostcode())) {
            changes.add("PostCode de envío: " + existingShipping.getPostcode() + " -> " + newShipping.getPostcode());
        }
        if (!existingShipping.getState().equals(newShipping.getState())) {
            changes.add("Departamento de envío: " + existingShipping.getState() + " -> " + newShipping.getState());
        }
        if (!existingShipping.getCity().equals(newShipping.getCity())) {
            changes.add("Ciudad de envío: " + existingShipping.getCity() + " -> " + newShipping.getCity());
        }
        if (!existingShipping.getCountry().equals(newShipping.getCountry())) {
            changes.add("País de envío: " + existingShipping.getCountry() + " -> " + newShipping.getCountry());
        }
        if (!existingShipping.getPriceShipping().equals(newShipping.getPriceShipping())) {
            changes.add("Valor del envío: " + existingShipping.getPriceShipping() + " -> " + newShipping.getPriceShipping());
        }
    }

    private void checkOrderChanges(OrderEntity existingOrder, OrderDTO newOrder, List<String> changes) {
        // Compara campos del pedido que no sean billing, shipping o productos
        if (!existingOrder.total.equals(newOrder.getTotal())) {
            changes.add("Total: " + existingOrder.total + " -> " + newOrder.getTotal());
        }
        if (!existingOrder.totalTax.equals(newOrder.getTotal_tax())) {
            changes.add("Total tax: " + existingOrder.totalTax + " -> " + newOrder.getTotal_tax());
        }
        if (!existingOrder.currency.equals(newOrder.getCurrency())) {
            changes.add("Moneda: " + existingOrder.currency + " -> " + newOrder.getCurrency());
        }
    }

    private void checkProductChanges(List<LineItem> existingItems, List<LineItem> newItems, long orderId) {
        List<String> addedProducts = new ArrayList<>();
        List<String> removedProducts = new ArrayList<>();
        List<String> updatedProducts = new ArrayList<>();

        // Verifica los productos agregados
        for (LineItem newItem : newItems) {
            boolean exists = existingItems.stream()
                    .anyMatch(item -> item.getProductId().equals(newItem.getProductId()));
            if (!exists) {
                addedProducts.add("Se agregó el producto: " + newItem.getName());
            } else {
                // Verifica si el producto ha cambiado (cantidad, subtotal, total, etc.)
                LineItem existingItem = existingItems.stream()
                        .filter(item -> item.getProductId().equals(newItem.getProductId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    List<String> changes = getChanges(existingItem, newItem);
                    if (!changes.isEmpty()) {
                        updatedProducts.add("Se actualizó el producto: " + newItem.getName() + " - Cambios: " + String.join(", ", changes));
                    }
                }
            }
        }

        // Verifica los productos eliminados
        for (LineItem existingItem : existingItems) {
            boolean exists = newItems.stream()
                    .anyMatch(item -> item.getProductId().equals(existingItem.getProductId()));
            if (!exists) {
                removedProducts.add("Se eliminó el producto: " + existingItem.getName());
            }
        }

        // Log de cambios
        if (!addedProducts.isEmpty()) {
            logChange(orderId, "Productos agregados", addedProducts);
        }
        if (!removedProducts.isEmpty()) {
            logChange(orderId, "Productos eliminados", removedProducts);
        }
        if (!updatedProducts.isEmpty()) {
            logChange(orderId, "Productos actualizados", updatedProducts);
        }
    }

    private List<String> getChanges(LineItem existingItem, LineItem newItem) {
        List<String> changes = new ArrayList<>();

        // Compara los campos y agrega el cambio correspondiente
        if (!existingItem.getQuantity().equals(newItem.getQuantity())) {
            changes.add("Cantidad: de " + existingItem.getQuantity() + " a " + newItem.getQuantity());
        }
        if (!existingItem.getSubtotal().equals(newItem.getSubtotal())) {
            changes.add("Subtotal: de " + existingItem.getSubtotal() + " a " + newItem.getSubtotal());
        }
        if (!existingItem.getTotal().equals(newItem.getTotal())) {
            changes.add("Total: de " + existingItem.getTotal() + " a " + newItem.getTotal());
        }
        if (!existingItem.getCodigoProveedor().equals(newItem.getCodigoProveedor())) {
            changes.add("Código Proveedor: de " + existingItem.getCodigoProveedor() + " a " + newItem.getCodigoProveedor());
        }

        return changes; // Retorna la lista de cambios
    }

    private void logChange(long orderId, String title, List<String> changes) {
        LogProduct changeLog = new LogProduct();
        changeLog.setTitle(title != null ? title : "Actualización del pedido");
        changeLog.setChanges(changes);
        changeLog.setChangeDate(LocalDateTime.now()); // Aseguramos que siempre haya una fecha
        changeLog.setOrderId(orderId);
        changeLog.persist();
    }

    // Sobrecarga para manejar un solo cambio como String
    private void logChange(long orderId, String title, String change) {
        List<String> changes = new ArrayList<>();
        changes.add(change);
        logChange(orderId, title, changes);
    }
}

