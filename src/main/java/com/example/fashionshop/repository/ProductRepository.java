package com.example.fashionshop.repository;

import com.example.fashionshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceBetweenOrderByPriceAsc(double minPrice, double maxPrice);
}
