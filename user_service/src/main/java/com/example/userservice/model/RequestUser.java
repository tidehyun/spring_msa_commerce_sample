package com.example.userservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
public class RequestUser {
    @NotNull(message = "Write Email!! ")
    @Size(min = 8, max = 255)
    @Email
    private String email;

    @NotNull(message = "Write PassWord!! ")
    @Size(min = 8, max = 20)
    private String pwd;

    @NotNull(message = "Write Name!! ")
    @Size(min = 2, max = 20)
    private String name;
}
