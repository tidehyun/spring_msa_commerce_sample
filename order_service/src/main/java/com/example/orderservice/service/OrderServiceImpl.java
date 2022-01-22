package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public OrderDto createOrder(OrderDto dto) {
        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQty() * dto.getUnitPrice());
        OrderEntity orderEntity = modelMapper.map(dto, OrderEntity.class);
        log.info("order request data : {}", orderEntity);
        repository.save(orderEntity);
        return dto;
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        Iterable<OrderEntity> orders = repository.findByUserId(userId);
        if (Objects.isNull(orders)) {
            throw new NoSuchElementException("Order not found");
        }
        return orders;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity order = repository.findByOrderId(orderId);
        if (Objects.isNull(order)) {
            throw new NoSuchElementException("Order not found");
        }
        return modelMapper.map(order, OrderDto.class);
    }
}
