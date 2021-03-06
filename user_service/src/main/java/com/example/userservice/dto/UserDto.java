package com.example.userservice.dto;

import com.example.userservice.model.ResponseOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@ToString
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String encPwd;
    private String userId;
    private Date createAt;

    private List<ResponseOrder> orders;
}
