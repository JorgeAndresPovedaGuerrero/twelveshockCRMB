package com.twelveshock.dao.contract;

import com.twelveshock.dto.OrderDTO;
import jakarta.ws.rs.core.Response;

import java.util.List;

public interface IOrdersDAO {
    List<OrderDTO> getOrders(String status, String startDate, String endDate);
}
