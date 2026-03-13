package com.example.fashionshop.dto;

import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.entity.Cart;
import com.example.fashionshop.entity.ProductVariant;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDTO {
    private Integer id;

    private Integer quantity;

    private ProductVariantResponse ProductVariantResponse;
}
