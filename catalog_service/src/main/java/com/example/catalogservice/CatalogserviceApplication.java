package com.example.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CatalogserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogserviceApplication.class, args);
    }

}
