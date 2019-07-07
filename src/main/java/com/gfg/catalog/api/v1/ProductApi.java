package com.gfg.catalog.api.v1;

import com.gfg.catalog.repository.ProductEntity;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/products")
@PreAuthorize("hasRole('ROLE_USER')")
public interface ProductApi {

    @GetMapping
    Page<Product> getProducts(@QuerydslPredicate(root = ProductEntity.class) Predicate predicate,
                              @PageableDefault(sort = {"title"}) Pageable pageable);

    @PostMapping(value = "/batch-insert")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    List<Product> insertProducts(@RequestBody @NotNull List<@Valid Product> newProducts);

    @PostMapping
    Product insertProduct(@RequestBody @NotNull @Valid Product newProduct);

    @GetMapping(value = "/{productId}")
    Product getProduct(@PathVariable(name = "productId") @NotNull UUID productId);

    @PutMapping(value = "/{productId}")
    Product updateProduct(@PathVariable(name = "productId") UUID productId, @RequestBody @NotNull @Valid Product product);

    @DeleteMapping(value = "/{productId}")
    void deleteProduct(@PathVariable(name = "productId") @NotNull UUID productId);
}
