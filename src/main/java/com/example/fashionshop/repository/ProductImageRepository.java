package com.example.fashionshop.repository;

import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.entity.ProductImage;
import com.example.fashionshop.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
