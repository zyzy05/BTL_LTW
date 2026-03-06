package com.example.fashionshop.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariantResponse {
    private Long id;
    private Integer stockQuantity;
    private SizeResponse size;
}
