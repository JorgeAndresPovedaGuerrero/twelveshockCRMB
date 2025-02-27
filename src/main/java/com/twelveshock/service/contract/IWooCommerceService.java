package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.Billing;
import com.twelveshock.dto.OrderDTO;

import java.util.List;

public interface IWooCommerceService {
    List<OrderDTO> getOrders(String status, String startDate, String endDate);

    OrderDTO createOrder(OrderDTO orderDTO, boolean isManual);

    OrderDTO updateOrder(long id, OrderDTO orderDTO);

    void deleteOrder(long id);
    OrderDTO getOrderById(long id);

    long getHighestOrderId();

    long getHighestClientId();
    Billing getBillingByClientId(long idCliente);
}
