package com.gfg.catalog.service;

import com.gfg.catalog.repository.ProductEntity;
import com.gfg.catalog.repository.ProductRepository;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    public Page<ProductEntity> getProducts(Predicate predicate, Pageable pageable) {
        return repository.findAll(predicate, pageable);
    }

    public List<ProductEntity> insertProducts(List<ProductEntity> newProducts) {
        return repository.saveAll(newProducts);
    }

    public ProductEntity insertProduct(ProductEntity newProduct) {
        return repository.save(newProduct);
    }

    public ProductEntity getProduct(UUID productId) {
            return repository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Unable to find Product with id " + productId));
    }

    public ProductEntity updateProduct(ProductEntity product) {
        UUID productId = product.getId();
        if (!repository.existsById(productId)){
            throw new ProductNotFoundException("Unable to find Product with id " + productId);
        }
        return repository.save(product);
    }

    public void deleteProduct(UUID productId) {
        repository.delete(getProduct(productId));
    }
}
