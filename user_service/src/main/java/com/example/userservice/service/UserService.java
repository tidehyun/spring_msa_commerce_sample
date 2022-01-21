package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;

public interface UserService {
    public UserDto createUser(UserDto dto);

    public Iterable<UserEntity> getUsers();

    public UserDto getUserById(String userId);
}
