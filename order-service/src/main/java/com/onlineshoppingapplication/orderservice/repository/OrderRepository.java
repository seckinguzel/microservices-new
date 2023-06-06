package com.onlineshoppingapplication.orderservice.repository;

import com.onlineshoppingapplication.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
