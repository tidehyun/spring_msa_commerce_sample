package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;

public interface OrderService {

    OrderDto createOrder(OrderDto orders);

    Iterable<OrderEntity> getOrdersByUserId(String userId);

    OrderDto getOrderByOrderId(String orderId);

}
