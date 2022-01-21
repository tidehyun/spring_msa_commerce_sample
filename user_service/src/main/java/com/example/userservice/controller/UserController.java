package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.RequestUser;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@Slf4j
public class UserController {

    UserService userService;

    ModelMapper modelMapper;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private String welcomeMsg;

    @GetMapping("/user/greeting")
    public String hello() {
        return welcomeMsg;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody RequestUser user, @Valid BindingResult result) {
        UserDto userDto = userService.createUser(modelMapper.map(user, UserDto.class));
        return new ResponseEntity<>(User.builder().userId(userDto.getUserId()).name(userDto.getName()).build(), HttpStatus.CREATED);
    }
}
