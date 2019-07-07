package com.gfg.catalog.repository;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    private String brand;
    @Column
    private BigDecimal price;
    @Column
    private String currency;
    @Column
    private String color;

    public ProductEntity() {
    }

    public ProductEntity(UUID id, String title, String description, String brand, BigDecimal price, String currency, String color) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.currency = currency;
        this.color = color;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductEntity entity = (ProductEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(title, entity.title) &&
                Objects.equals(description, entity.description) &&
                Objects.equals(brand, entity.brand) &&
                Objects.equals(price, entity.price) &&
                Objects.equals(currency, entity.currency) &&
                Objects.equals(color, entity.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, brand, price, currency, color);
    }
}
