package com.ecommerce.microcommerce.dao;

import com.ecommerce.microcommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    List<Product> findAll();
    Product findById(int id);
    Product save(Product product);
    List<Product> findByPriceGreaterThan(int limitPrice);

    @Query("SELECT p FROM Product p WHERE p.price > :limitPrice")
    List<Product> findProductMoreExpensive(@Param("limitPrice") int price);
}
