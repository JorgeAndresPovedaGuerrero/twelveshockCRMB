package com.twelveshock.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.twelveshock.dao.entity.Billing;
import com.twelveshock.dao.entity.OrderEntity;
import com.twelveshock.dao.impl.OrdersDAO;
import com.twelveshock.dto.OrderDTO;
import com.twelveshock.facade.WoocommerceClient;
import com.twelveshock.service.contract.IWooCommerceService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;


import java.util.List;

@ApplicationScoped
public class WooCommerceServiceImpl implements IWooCommerceService {
    @Inject
    OrdersDAO ordersDAO;

    @Override
    public List<OrderDTO> getOrders(String status, String startDate, String endDate) {
        return ordersDAO.getOrders(status, startDate, endDate);
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        return ordersDAO.createOrder(orderDTO);
    }

    @Override
    public OrderDTO updateOrder(long id, OrderDTO orderDTO) {
        return ordersDAO.updateOrder(id, orderDTO);
    }

    public OrderDTO getOrderById(long id) {
        OrderEntity orderEntity = OrderEntity.findById(id);
        if (orderEntity == null) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.convertValue(orderEntity, OrderDTO.class);
    }

    @Override
    public void deleteOrder(long id) {
        ordersDAO.deleteOrder(id);
    }

    @Override
    public long getHighestOrderId() {
        return ordersDAO.getHighestOrderId();
    }

    @Override
    public long getHighestClientId() {
        return ordersDAO.getHighestClientId();
    }
    @Override
    public Billing getBillingByClientId(long idCliente) {
        return ordersDAO.findBillingByClientId(idCliente);
    }
}