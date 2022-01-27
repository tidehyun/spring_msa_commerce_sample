package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.model.RequestUser;
import com.example.userservice.model.ResponseUser;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Slf4j
public class UserController {

    private UserService userService;

    private ModelMapper modelMapper;

    private final static String KEY_EMAIL = "email";
    private final static String KEY_PWD = "pwd";

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    private String welcomeMsg;

    @GetMapping("/greeting")
    public String hello() {
        return welcomeMsg;
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody RequestUser user, @Valid BindingResult result) {
        UserDto userDto = userService.createUser(modelMapper.map(user, UserDto.class));
        return new ResponseEntity<>(User.builder().userId(userDto.getUserId()).name(userDto.getName()).build(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public void login(Map<String, String> loginMap) {
        if (Objects.nonNull(loginMap) && Objects.nonNull(loginMap.get(KEY_EMAIL)) && Objects.nonNull(loginMap.get(KEY_PWD))) {
        } else {
            throw new RuntimeException("ID or PW is empty");
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> getUsers() {
        List<ResponseUser> users = new ArrayList<>();
        userService.getUsers().forEach(userEntity -> users.add(modelMapper.map(userEntity, ResponseUser.class)));
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
        ResponseUser user = modelMapper.map(userService.getUserById(userId), ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
