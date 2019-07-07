package com.gfg.catalog.repository;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryITest {

    @Autowired
    private ProductRepository repository;

    @After
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    public void getAll() {
        ProductEntity entity1 = repository.save(new ProductEntity());
        ProductEntity entity2 = repository.save(new ProductEntity());

        List<ProductEntity> actual = repository.findAll();

        assertThat(actual).containsExactly(entity1, entity2);
    }

    @Test
    public void getProductById() {
        ProductEntity product = new ProductEntity();
        ProductEntity savedProduct = repository.save(product);

        ProductEntity actual = repository.getOne(savedProduct.getId());

        assertThat(actual).isEqualToComparingFieldByFieldRecursively(savedProduct);
    }
}