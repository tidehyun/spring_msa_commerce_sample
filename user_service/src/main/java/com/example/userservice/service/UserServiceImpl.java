package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.plaf.multi.MultiOptionPaneUI;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository repository;
    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public UserDto createUser(UserDto dto) {
        byte[] arr = new byte[7];
        new Random().nextBytes(arr);
        dto.setUserId(RandomStringUtils.randomAlphabetic(10));

        UserEntity userEntity = modelMapper.map(dto, UserEntity.class);
        userEntity.setEncPwd(UUID.randomUUID().toString());
        log.info("user data : {}", userEntity);
        repository.save(userEntity);
        return dto;
    }
}
