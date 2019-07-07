package com.gfg.catalog.service;

import com.gfg.catalog.repository.ProductEntity;
import com.gfg.catalog.repository.ProductRepository;
import com.querydsl.core.types.Predicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService sut;

    @Test
    public void shouldGetProducts() {
        Predicate predicate = mock(Predicate.class);
        Pageable pageable = mock(Pageable.class);
        Page<ProductEntity> products = mock(Page.class);
        when((repository.findAll(predicate, pageable))).thenReturn(products);

        Page<ProductEntity> actual = sut.getProducts(predicate, pageable);

        assertThat(actual).isEqualTo(products);
    }

    @Test
    public void shouldInsertProducts() {
        List<ProductEntity> products = mock(List.class);
        List<ProductEntity> insertedProducts = mock(List.class);
        when(repository.saveAll(products)).thenReturn(insertedProducts);

        List<ProductEntity> actual = sut.insertProducts(products);

        assertThat(actual).isEqualTo(insertedProducts);
    }

    @Test
    public void shouldInsertProduct() {
        ProductEntity product = mock(ProductEntity.class);
        ProductEntity savedProduct = mock(ProductEntity.class);
        when(repository.save(product)).thenReturn(savedProduct);

        ProductEntity actual = sut.insertProduct(product);

        assertThat(actual).isEqualTo(savedProduct);
    }

    @Test
    public void shouldGetProduct() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = mock(ProductEntity.class);
        when(repository.findById(productId)).thenReturn(Optional.of(product));

        ProductEntity actual = sut.getProduct(productId);

        assertThat(actual).isEqualTo(product);
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProduct_shouldThrowExceptionWhenNotExist() {
        sut.getProduct(UUID.randomUUID());
    }

    @Test
    public void shouldUpdateProduct() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = mock(ProductEntity.class);
        when(product.getId()).thenReturn(productId);
        when(repository.existsById(productId)).thenReturn(true);
        ProductEntity savedProduct = mock(ProductEntity.class);
        when(repository.save(product)).thenReturn(savedProduct);

        ProductEntity actual = sut.updateProduct(product);

        assertThat(actual).isEqualTo(savedProduct);
    }

    @Test(expected = ProductNotFoundException.class)
    public void updateProduct_shouldThrowExceptionWhenNotExist() {
        UUID productId = UUID.randomUUID();
        ProductEntity product = mock(ProductEntity.class);
        when(product.getId()).thenReturn(productId);
        when(repository.existsById(productId)).thenReturn(false);

        sut.updateProduct(product);
    }

    @Test(expected = ProductNotFoundException.class)
    public void deleteProduct_shouldThrowExceptionWhenNotExist() {
        UUID productId = UUID.randomUUID();

        sut.deleteProduct(productId);
    }
}