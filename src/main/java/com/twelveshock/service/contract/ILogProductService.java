package com.twelveshock.service.contract;

import com.twelveshock.dao.entity.LogProduct;

import java.util.List;

public interface ILogProductService {
    List<LogProduct> getLogsByOrderId(long orderId);
}
