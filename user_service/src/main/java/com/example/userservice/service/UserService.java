package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public UserDto createUser(UserDto dto);

    public Iterable<UserEntity> getUsers();

    public UserDto getUserById(String userId);

    public UserDto getUserDetailsByEmail(String userName);
}
