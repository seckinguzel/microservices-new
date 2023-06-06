package com.onlineshoppingapplication.orderservice.service;

import com.onlineshoppingapplication.orderservice.dto.OrderRequest;

public interface OrderService {
    void placeOrder(OrderRequest orderRequest);
}
