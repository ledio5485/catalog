package com.gfg.catalog.api.v1;

import com.gfg.catalog.repository.ProductEntity;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductConverterTest {

    private final ProductConverter sut = new ProductConverter(new ModelMapper());

    @Test
    public void fromDto() {
        UUID fixedUUID = UUID.randomUUID();
        Product product = new Product(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR", "p1 color");

        ProductEntity actual = sut.fromDto(product);

        ProductEntity expected = new ProductEntity(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR", "p1 color");
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void fromListOfDto() {
        UUID fixedUUID = UUID.randomUUID();
        Product dto1 = new Product(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR", "p1 color");
        UUID fixedUUID2 = UUID.randomUUID();
        Product dto2 = new Product(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56), "EUR","p2 color");

        List<ProductEntity> actual = sut.fromDto(Lists.list(dto1, dto2));

        ProductEntity entity1 = new ProductEntity(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");
        ProductEntity entity2 = new ProductEntity(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56),"EUR", "p2 color");

        assertThat(actual).containsExactly(entity1, entity2);
    }

    @Test
    public void toDto() {
        UUID fixedUUID = UUID.randomUUID();
        ProductEntity entity = new ProductEntity(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");

        Product actual = sut.toDto(entity);

        Product expected = new Product(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");
        assertThat(actual).isEqualToComparingFieldByFieldRecursively(expected);
    }

    @Test
    public void toListOfDto() {
        UUID fixedUUID = UUID.randomUUID();
        ProductEntity entity1 = new ProductEntity(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54),"EUR", "p1 color");
        UUID fixedUUID2 = UUID.randomUUID();
        ProductEntity entity2 = new ProductEntity(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56), "EUR","p2 color");

        List<Product> actual = sut.toDto(Lists.list(entity1, entity2));

        Product dto1 = new Product(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");
        Product dto2 = new Product(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56), "EUR","p2 color");

        assertThat(actual).containsExactly(dto1, dto2);
    }

    @Test
    public void toPageOfDto() {
        UUID fixedUUID = UUID.randomUUID();
        ProductEntity entity1 = new ProductEntity(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");
        UUID fixedUUID2 = UUID.randomUUID();
        ProductEntity entity2 = new ProductEntity(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56), "EUR","p2 color");
        Page<ProductEntity> pageOfEntities = new PageImpl<>(Lists.list(entity1, entity2));

        Page<Product> actual = sut.toPageDto(pageOfEntities);

        Product dto1 = new Product(fixedUUID, "p1 title", "p1 description", "p1 brand", new BigDecimal(6.54), "EUR","p1 color");
        Product dto2 = new Product(fixedUUID2, "p2 title", "p2 description", "p2 brand", new BigDecimal(4.56),"EUR", "p2 color");
        Page<Product> expected = new PageImpl<>(Lists.list(dto1, dto2));
        assertThat(actual.getContent()).containsExactlyElementsOf(expected.getContent());
    }

}