package com.example.catalogservice.service;

import com.example.catalogservice.dto.CatalogDto;
import com.example.catalogservice.entity.CatalogEntity;
import com.example.catalogservice.repository.CatalogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class CatalogServiceImpl implements CatalogService {

    private CatalogRepository repository;

    public CatalogServiceImpl(CatalogRepository repository) {
        this.repository = repository;
    }

    private ModelMapper modelMapper;

    @PostConstruct
    public void init() {
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return repository.findAll();
    }

    @Override
    public CatalogDto getCatalogByProductId(String productId) {
        CatalogEntity catalogEntity = repository.findByProductId(productId);
        if (Objects.isNull(catalogEntity)) {
            throw new NoSuchElementException("Catalog not found");
        }
        return modelMapper.map(catalogEntity, CatalogDto.class);
    }
}
