package com.gfg.catalog.api.v1;

import com.gfg.catalog.service.ProductService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ProductController implements ProductApi {

    private final ProductService productService;
    private final ProductConverter converter;

    public ProductController(ProductService productService, ProductConverter converter) {
        this.productService = productService;
        this.converter = converter;
    }

    @Override
    public Page<Product> getProducts(Predicate predicate, Pageable pageable) {
        return converter.toPageDto(productService.getProducts(predicate, pageable));
    }

    @Override
    public List<Product> insertProducts(List<Product> newProducts) {
        return converter.toDto(productService.insertProducts(converter.fromDto(newProducts)));
    }

    @Override
    public Product insertProduct(Product newProduct) {
        return converter.toDto(productService.insertProduct(converter.fromDto(newProduct)));
    }

    @Override
    public Product getProduct(UUID productId) {
        return converter.toDto(productService.getProduct(productId));
    }

    @Override
    public Product updateProduct(UUID productId, Product product) {
        assert(productId.equals(product.getId()));
        return converter.toDto(productService.updateProduct(converter.fromDto(product)));
    }

    @Override
    public void deleteProduct(UUID productId) {
        productService.deleteProduct(productId);
    }

}
