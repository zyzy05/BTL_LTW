package com.example.fashionshop.dto.response;


import com.example.fashionshop.entity.ProductImage;
import com.example.fashionshop.entity.ProductVariant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductsResponse {
    private String name;
    private String description;
    private Double price;
    private List<ProductImageResponse> images;
    private List<ProductVariantResponse> productVariants;
}
