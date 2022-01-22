package com.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Setter
@Getter
@ToString
public class OrderDto implements Serializable {

    private String productId;
    private String orderId;
    private String userId;

    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;

}
