package com.example.userservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Date;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
    private String orderId;
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createAt;
}
