package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.model.RequestOrder;
import com.example.orderservice.model.ResponseOrder;
import com.example.orderservice.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
@Slf4j
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @PostMapping("/order/{userId}")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder requestOrder) {
        OrderDto dto = modelMapper.map(requestOrder, OrderDto.class);
        dto.setUserId(userId);
        dto = orderService.createOrder(dto);
        ResponseOrder responseOrder = modelMapper.map(dto, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/orders/{userId}")
    public ResponseEntity<List<ResponseOrder>> getOrdersByUserId(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orders = orderService.getOrdersByUserId(userId);
        List<ResponseOrder> orderList = new ArrayList<>();
        orders.forEach(orderEntity -> orderList.add(modelMapper.map(orderEntity, ResponseOrder.class)));
        return ResponseEntity.status(HttpStatus.OK).body(orderList);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseOrder> getOrderByOrderId(@PathVariable("orderId") String orderId) {
        OrderDto dto = orderService.getOrderByOrderId(orderId);
        ResponseOrder order = modelMapper.map(dto, ResponseOrder.class);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }
}
