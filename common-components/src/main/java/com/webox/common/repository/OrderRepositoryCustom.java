package com.webox.common.repository;

import java.util.List;

import com.webox.common.model.Order;

public interface OrderRepositoryCustom {
    public List<Order> loadOrdersByService(String serviceId);
    public List<Order> loadOrdersByVendor(String vendorId);
    public List<Order> loadOrdersByCustomer(String userId);
    public List<Order> loadOrdersByServiceAndUser(String serviceId,String userId);

}