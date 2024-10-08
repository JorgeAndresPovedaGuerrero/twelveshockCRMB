package com.twelveshock.service.impl;

import com.twelveshock.dao.entity.LogProduct;
import com.twelveshock.service.contract.ILogProductService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class LogProductServiceImpl implements ILogProductService {

    @Override
    public List<LogProduct> getLogsByOrderId(long orderId) {
        return LogProduct.find("orderId", orderId).list();
    }
}

