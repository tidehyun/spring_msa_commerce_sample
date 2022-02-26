package com.example.userservice.client;

import com.example.userservice.model.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceFeignClient {

    @GetMapping("/order-service/orders/{userId}")
    List<ResponseOrder> getOrderS(@PathVariable String userId);
}
