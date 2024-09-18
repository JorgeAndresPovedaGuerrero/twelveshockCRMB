package com.twelveshock.dao.impl;

import com.twelveshock.dao.contract.ITransaccionDAO;
import com.twelveshock.dao.entity.OrderEntity;

import java.util.List;

import static io.quarkus.mongodb.panache.PanacheMongoEntityBase.*;

public class TransaccionDAO implements ITransaccionDAO {
    public List<OrderEntity> getOrders() {
        return listAll();
    }

    public OrderEntity findById(long id) {
        return find("id", id).firstResult();
    }

    public void saveOrder(OrderEntity order) {
        persist(order);
    }

    public void updateOrder(OrderEntity order) {
        update(order);
    }

    public void deleteOrder(long id) {
        delete("id", id);
    }
}
