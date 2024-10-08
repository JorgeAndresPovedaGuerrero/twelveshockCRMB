package com.twelveshock.dao.contract;

import com.twelveshock.dto.OrderDTO;

import java.util.List;

public interface IOrdersDAO {
    List<OrderDTO> getOrders(String status, String startDate, String endDate);
}
