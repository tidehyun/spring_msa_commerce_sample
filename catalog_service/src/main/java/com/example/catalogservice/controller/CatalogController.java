package com.example.catalogservice.controller;

import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.model.ResponseCatalog;
import com.example.catalogservice.service.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/catalog-service")
@Slf4j
public class CatalogController {
    private CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> catalogEntities = catalogService.getAllCatalogs();
        List<ResponseCatalog> catalogList = new ArrayList<>();

        catalogEntities.forEach(catalogEntity -> catalogList.add(modelMapper.map(catalogEntity, ResponseCatalog.class)));
        return ResponseEntity.status(HttpStatus.OK).body(catalogList);
    }
}
