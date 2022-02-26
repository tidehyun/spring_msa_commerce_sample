package com.example.userservice.service;

import com.example.userservice.client.OrderServiceFeignClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.model.ResponseOrder;
import com.example.userservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository repository;
    ModelMapper modelMapper;
    BCryptPasswordEncoder passwordEncoder;
    RestTemplate restTemplate;
    OrderServiceFeignClient orderServiceFeignClient;

    @Value("${order.service.url}")
    private String orderSvcUrl;

    public UserServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder, RestTemplate restTemplate, OrderServiceFeignClient orderServiceFeignClient) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
        this.orderServiceFeignClient = orderServiceFeignClient;
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

        // orders by rest template
//        ResponseEntity<List<ResponseOrder>> exchange = usingRestClientTemplate(userDto);
//        userDto.setOrders(exchange.getBody());

        // orders by feign client
        userDto.setOrders(usingFeignClient(userDto.getUserId()));
        return userDto;
    }

    private ResponseEntity<List<ResponseOrder>> usingRestClientTemplate(UserDto userDto) {
        String orderUrl = orderSvcUrl.concat("/orders/").concat(userDto.getUserId());
        log.info("order service url : {}", orderUrl);
        return restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
        });
    }

    private List<ResponseOrder> usingFeignClient(String userId) {
        return orderServiceFeignClient.getOrderS(userId);
    }

    @Override
    public UserDto getUserDetailsByEmail(String userName) {
        UserEntity userEntity = repository.findByEmail(userName);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found");
        }
        UserDto userDto = modelMapper.map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity userEntity = repository.findByEmail(email);
        log.info("load user by email : {}", userEntity);
        if (Objects.isNull(userEntity)) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(userEntity.getEmail(), userEntity.getEncPwd(), new ArrayList<>());
    }
}
