package com.ecommerce.microcommerce.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
public class ProductController {

    private final ProductDao productDao;

    public ProductController(ProductDao productDao) {
        this.productDao = productDao;
    }

    @GetMapping("/products")
    public MappingJacksonValue productsList() {
        List<Product> productList = productDao.findAll();
        SimpleBeanPropertyFilter testFilter = SimpleBeanPropertyFilter.serializeAllExcept("purchasePrice");
        FilterProvider filterList = new SimpleFilterProvider().addFilter("testFilterDynamic", testFilter);
        MappingJacksonValue productFilter = new MappingJacksonValue(productList);
        productFilter.setFilters(filterList);
        return productFilter;
    }

    @GetMapping(value = "/products/{id}")
    public MappingJacksonValue productById(@PathVariable int id) {
        Product product = productDao.findById(id);
        SimpleBeanPropertyFilter testFilter = SimpleBeanPropertyFilter.serializeAllExcept("purchasePrice");
        FilterProvider filterList = new SimpleFilterProvider().addFilter("testFilterDynamic", testFilter);
        MappingJacksonValue productFilter = new MappingJacksonValue(product);
        productFilter.setFilters(filterList);
        return productFilter;
    }

    @GetMapping(value = "/products/limit/{limitPrice}")
    public MappingJacksonValue productsListByLimitPrice(@PathVariable int limitPrice)  {
        List<Product> productList = productDao.findByPriceGreaterThan(limitPrice);
        SimpleBeanPropertyFilter testFilter = SimpleBeanPropertyFilter.serializeAllExcept("purchasePrice");
        FilterProvider filterList = new SimpleFilterProvider().addFilter("testFilterDynamic", testFilter);
        MappingJacksonValue productFilter = new MappingJacksonValue(productList);
        productFilter.setFilters(filterList);
        return productFilter;
    }

    @GetMapping(value = "/products/expensive/limit/{limitPrice}")
    public MappingJacksonValue productMoreExpensiveByLimitPrice(@PathVariable int limitPrice)  {
        List<Product> productList = productDao.findProductMoreExpensive(limitPrice);
        SimpleBeanPropertyFilter testFilter = SimpleBeanPropertyFilter.serializeAllExcept("purchasePrice");
        FilterProvider filterList = new SimpleFilterProvider().addFilter("testFilterDynamic", testFilter);
        MappingJacksonValue productFilter = new MappingJacksonValue(productList);
        productFilter.setFilters(filterList);
        return productFilter;
    }

    @PostMapping(value = "/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product productAdded = productDao.save(product);
        if (Objects.isNull(productAdded)) {
            return ResponseEntity.noContent().build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping (value = "/products")
    public void updateProduct(@RequestBody Product product)
    {
        productDao.save(product);
    }

    @DeleteMapping (value = "/products/{id}")
    public void deleteProduct(@PathVariable int id) {
        productDao.deleteById(id);
    }
}
