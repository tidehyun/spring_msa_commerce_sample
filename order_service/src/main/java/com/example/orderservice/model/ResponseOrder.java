package com.example.orderservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@ToString
public class ResponseOrder {
    private String productId;
    private String orderId;
    private String userId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createDate;
}

