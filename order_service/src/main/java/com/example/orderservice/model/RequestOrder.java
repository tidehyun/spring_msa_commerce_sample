package com.example.orderservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
}
