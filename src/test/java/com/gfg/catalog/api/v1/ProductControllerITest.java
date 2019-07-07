package com.gfg.catalog.api.v1;

import com.gfg.catalog.CatalogApplication;
import com.gfg.catalog.api.PageWrapper;
import com.gfg.catalog.repository.ProductRepository;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CatalogApplication.class)
public class ProductControllerITest {

    private static final String API_V1_PRODUCTS = "/api/v1/products";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ProductRepository productRepository;

    @After
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void getSortedSliceOfProducts() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        Product p2 = new Product(null, "p2 title", "p2 description", "p2 brand", new BigDecimal("4.56"), "EUR", "p2 color");
        List<Product> insertedProducts = insertProducts(Lists.list(p1, p2));

        Page<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "?page=0&size=10&sort=brand,DESC", HttpMethod.GET, httpEntity("user", "user"), new ParameterizedTypeReference<PageWrapper<Product>>() {
        }).getBody();

        assertNotNull(actual);
        assertThat(actual.getContent()).containsExactly(insertedProducts.get(1), insertedProducts.get(0));
    }

    @Test
    public void getFilteredSliceOfProducts() {
        Product p1 = new Product(null, "title 1", "p1 description", "brand 1", new BigDecimal("1.23"), "EUR", "red");
        Product p2 = new Product(null, "title 1", "p2 description", "brand 2", new BigDecimal("4.56"), "EUR", "green");
        Product p3 = new Product(null, "title 2", "p3 description", "brand 2", new BigDecimal("7.89"), "EUR", "blue");
        List<Product> insertedProducts = insertProducts(Lists.list(p1, p2, p3));

        Page<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "?page=0&size=10&sort=brand,DESC&title=title 1&brand=brand 2", HttpMethod.GET, httpEntity("user", "user"), new ParameterizedTypeReference<PageWrapper<Product>>() {
        }).getBody();

        assertNotNull(actual);
        assertThat(actual.getContent()).containsExactly(insertedProducts.get(1));
    }

    @Test
    public void getProducts_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "?page=0&size=10&sort=brand,DESC", HttpMethod.GET, null, Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getProducts_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "?page=0&size=10&sort=brand,DESC", HttpMethod.GET, httpEntity("user1", "user1"), Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void insertProduct() {
        Product product = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "red");

        ResponseEntity<Product> actual = this.restTemplate.postForEntity(API_V1_PRODUCTS, httpEntity(product, "user", "user"), Product.class);

        assertThat(actual.getBody()).isEqualToIgnoringGivenFields(product, "id");
    }

    @Test
    public void insertProduct_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        Product product = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "red");

        ResponseEntity<Product> actual = this.restTemplate.postForEntity(API_V1_PRODUCTS, httpEntity(product, "user2", "user2"), Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void insertProduct_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        Product product = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "red");

        ResponseEntity<Product> actual = this.restTemplate.postForEntity(API_V1_PRODUCTS, httpEntity(product, "user1", "user1"), Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void insertProducts() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        Product p2 = new Product(null, "p2 title", "p2 description", "p2 brand", new BigDecimal("4.56"), "EUR", "p2 color");

        List<Product> actual = insertProducts(Lists.list(p1, p2));

        Page<Product> expected = this.restTemplate.exchange(API_V1_PRODUCTS, HttpMethod.GET, httpEntity("user", "user"), new ParameterizedTypeReference<PageWrapper<Product>>() {
        }).getBody();

        assertNotNull(expected);
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected.getContent());
    }

    @Test
    public void insertProducts_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        Product p2 = new Product(null, "p1 title", "p2 description", "p2 brand", new BigDecimal("4.56"), "EUR", "p2 color");

        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/batch-insert", HttpMethod.POST, new HttpEntity<>(Lists.list(p1, p2)), Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void insertProducts_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        Product p2 = new Product(null, "p1 title", "p2 description", "p2 brand", new BigDecimal("4.56"), "EUR", "p2 color");

        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/batch-insert", HttpMethod.POST, httpEntity(Lists.list(p1, p2), "user", "user"), Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getProduct() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        Product insertedProduct = insertProducts(Collections.singletonList(p1)).get(0);

        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + insertedProduct.getId(), HttpMethod.GET, httpEntity("user", "user"), Product.class);

        assertThat(actual.getBody()).isEqualToComparingFieldByFieldRecursively(insertedProduct);
    }

    @Test
    public void getProduct_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.GET, null, Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void getProduct_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.GET, httpEntity("user1", "user1"), Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void getProduct_shouldReturn404WhenProductDoesNotExist() {
        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.GET, httpEntity("user", "user"), Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateProduct() {
        Product insertedProduct = insertProducts(Collections.singletonList(new Product())).get(0);
        Product productToUpdate = new Product(insertedProduct.getId(), "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");

        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + insertedProduct.getId(), HttpMethod.PUT, httpEntity(productToUpdate, "user", "user"), Product.class);

        assertThat(actual.getBody()).isEqualToComparingFieldByField(productToUpdate);
    }

    @Test
    public void updateProduct_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        UUID uuid = UUID.randomUUID();
        Product productToUpdate = new Product(uuid, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");

        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + uuid, HttpMethod.PUT, new HttpEntity<>(productToUpdate), Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void updateProduct_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        UUID uuid = UUID.randomUUID();
        Product productToUpdate = new Product(uuid, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");

        ResponseEntity<Object> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + uuid, HttpMethod.PUT, httpEntity(productToUpdate, "user1", "user1"), Object.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void updateProduct_shouldReturn404WhenProductDoesNotExist() {
        UUID uuid = UUID.randomUUID();
        Product productToUpdate = new Product(uuid, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");

        ResponseEntity<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + uuid.toString(), HttpMethod.PUT, httpEntity(productToUpdate, "user", "user"), Product.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteProduct() {
        Product p1 = new Product(null, "p1 title", "p1 description", "p1 brand", new BigDecimal("6.54"), "EUR", "p1 color");
        List<Product> insertedProducts = insertProducts(Collections.singletonList(p1));

        assertEquals(1, insertedProducts.size());

        this.restTemplate.exchange(API_V1_PRODUCTS + "/" + insertedProducts.get(0).getId(), HttpMethod.DELETE, httpEntity("user", "user"), Void.class);

        Page<Product> actual = this.restTemplate.exchange(API_V1_PRODUCTS, HttpMethod.GET, httpEntity("user", "user"), new ParameterizedTypeReference<PageWrapper<Product>>() {
        }).getBody();

        assertNotNull(actual);
        assertTrue(actual.getContent().isEmpty());
    }

    @Test
    public void deleteProduct_shouldReturn401WhenAnonymousUserTriesToMakeRequest() {
        ResponseEntity<Void> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.DELETE, null, Void.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void deleteProduct_shouldReturn403WhenUserDoesNotHaveEnoughPrivilege() {
        ResponseEntity<Void> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.DELETE, httpEntity("user1", "user1"), Void.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void deleteProduct_shouldReturn404WhenProductDoesNotExist() {
        ResponseEntity<Void> actual = this.restTemplate.exchange(API_V1_PRODUCTS + "/" + UUID.randomUUID().toString(), HttpMethod.DELETE, httpEntity("user", "user"), Void.class);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private List<Product> insertProducts(List<Product> newProducts) {
        return this.restTemplate.exchange(API_V1_PRODUCTS + "/batch-insert", HttpMethod.POST, httpEntity(newProducts, "admin", "admin"), new ParameterizedTypeReference<List<Product>>() {
        }).getBody();
    }

    private HttpEntity<Object> httpEntity(String username, String password) {
        return new HttpEntity<>(null, authorizedHeaders(username, password));
    }

    private HttpEntity<Object> httpEntity(Object payload, String username, String password) {
        return new HttpEntity<>(payload, authorizedHeaders(username, password));
    }

    private HttpHeaders authorizedHeaders(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.defaultCharset()));
        return new HttpHeaders() {{
            set("Authorization", "Basic " + new String(encodedAuth));
        }};
    }
}