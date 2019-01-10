package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Order;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String>, OrderRepositoryCustom {

    public List<Order> findByUserId(String UserId);

    public Order findByOrderId(String orderId);

}