package com.example.userservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RequestLogin {

    private String email;
    private String pwd;
}
