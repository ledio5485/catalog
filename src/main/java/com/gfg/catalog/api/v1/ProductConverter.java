package com.gfg.catalog.api.v1;

import com.gfg.catalog.repository.ProductEntity;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
class ProductConverter {
    private final ModelMapper modelMapper;

    ProductConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    ProductEntity fromDto(Product product) {
        return modelMapper.map(product, ProductEntity.class);
    }

    List<ProductEntity> fromDto(List<Product> products) {
        return products.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());
    }

    Product toDto(ProductEntity entity) {
        return modelMapper.map(entity, Product.class);
    }

    List<Product> toDto(List<ProductEntity> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    Page<Product> toPageDto(Page<ProductEntity> entities) {
        return entities.map(this::toDto);
    }
}
