# Catalog API Service

## Description

This a basic CRUD API for products, which also includes a functionality for batch creation and fetch. 
A product consists of the following attributes:

- id
- title
- description
- brand
- price
- color

## Additional Features

- Versioned API
- Implementation done with Spring Boot in Java with H2 (an in memory database) and JPA
- Secured the API endpoint with basic authentication.
- Provided a simple search by title and description
- The price, brand and color are sortable
- Provided pagination

## Prerequisites:
* [java 8+][java-download]
* [maven 3.x.x][maven-download]
* [docker][docker-website] (optional)

Check the installed version of java
```bash
java -version
```
and maven
```bash
mvn --v
```

## Assumptions:

To simplify a bit the task, there are some assumptions:

* The `price` field is a decimal value. In the real world it should be an object that holds the amount, the currency code(ISO 4217) and rounding. (see Money and Currency in java 9)
* The `brand` field is a string. Normally it should be another entity(table), and the product holds only the reference(foreign key) of the brand.id(primary key)
* H2 DB is used as a persistence layer. The repository loads and store in the memory. If you want to persist in FS, please change the configuration. 

##Test, Build and Run instructions

- test:  
```bash
mvn clean verify
```
- package: 
```bash
mvn clean package
```
- run: 

if you have docker already installed, run this command
```bash
mvn clean package -Dmaven.test.skip=true && mvn com.spotify:dockerfile-maven-plugin:build && docker run -p 8080:8080 ledion/catalog-api-service:latest
```
otherwise
```bash
mvn clean package -Dmaven.test.skip=true && java -jar target/catalog-api-service-1.0.0-SNAPSHOT.jar
```

## API Documentation

You can consult the REST API spec [here][api-docs].

## Test the REST

You can try it [at this page][swagger-ui].


[java-download]: https://www.java.com/en/download/
[maven-download]: https://maven.apache.org/download.cgi
[docker-website]: https://www.docker.com/

[api-docs]: http://localhost:8080/catalog/v2/api-docs
[swagger-ui]: http://localhost:8080/catalog/swagger-ui.html#/product-controller
