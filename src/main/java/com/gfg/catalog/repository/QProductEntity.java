package com.gfg.catalog.repository;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;

import javax.annotation.Generated;

import com.querydsl.core.types.Path;

import java.math.BigDecimal;


/**
 * QProductEntity is a Querydsl query type for ProductEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QProductEntity extends EntityPathBase<ProductEntity> {

    private static final long serialVersionUID = 1560940598L;

    public static final QProductEntity productEntity = new QProductEntity("productEntity");

    public final StringPath brand = createString("brand");

    public final StringPath color = createString("color");

    public final StringPath description = createString("description");

    public final ComparablePath<java.util.UUID> id = createComparable("id", java.util.UUID.class);

    public final NumberPath<BigDecimal> price = createNumber("price", BigDecimal.class);

    public final StringPath currency = createString("currency");

    public final StringPath title = createString("title");

    public QProductEntity(String variable) {
        super(ProductEntity.class, forVariable(variable));
    }

    public QProductEntity(Path<? extends ProductEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProductEntity(PathMetadata metadata) {
        super(ProductEntity.class, metadata);
    }

}

