package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository repository;
    ModelMapper modelMapper;
    BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UserDto createUser(UserDto dto) {
        dto.setUserId(RandomStringUtils.randomAlphabetic(10));

        UserEntity userEntity = modelMapper.map(dto, UserEntity.class);
        userEntity.setEncPwd(passwordEncoder.encode(dto.getPwd()));
        log.info("user data : {}", userEntity);
        repository.save(userEntity);
        return dto;
    }

    @Override
    public Iterable<UserEntity> getUsers() {
        return repository.findAll();
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = repository.findByUserId(userId);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        userDto.setOrders(new ArrayList<>());
        return userDto;
    }
}
