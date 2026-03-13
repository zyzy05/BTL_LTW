package com.example.fashionshop.service;

import com.example.fashionshop.dto.CartItemDTO;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.entity.*;
import com.example.fashionshop.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartItemService {
    @Autowired
            private CartItemRepository cartItemRepository;
    public CartItemDTO getCartItemById(Long id) {
        CartItem cartItem = cartItemRepository.findById(id).orElse(null);
        CartItemDTO cartItemDTO = new CartItemDTO();

        cartItemDTO.setQuantity(cartItem.getQuantity());
        ProductVariant productVariant = cartItem.getProductVariant();
        ProductVariantResponse productVariantResponse = new ProductVariantResponse();
        productVariantResponse.setId(productVariant.getId());

        Size size  = productVariant.getSize();
        SizeResponse sizeResponse = new SizeResponse();

        sizeResponse.setName(size.getName());
        Product product = productVariant.getProduct();
        List<ProductImage> productImageList = product.getImages();
        List<ProductImageResponse> productImageResponseList = new ArrayList<>();
        for(ProductImage productImage : productImageList){
            ProductImageResponse productImageResponse = new ProductImageResponse();
            productImageResponse.setImageUrl(productImage.getImageUrl());
            productImageResponseList.add(productImageResponse);
        }

        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setName(product.getName());

        productsResponse.setPrice(product.getPrice());
        productsResponse.setDescription(product.getDescription());
        productsResponse.setImages(productImageResponseList);
        productVariantResponse.setProductResponse(productsResponse);
        productVariantResponse.setSize(sizeResponse);
        productVariantResponse.setStockQuantity(productVariant.getStockQuantity());

        cartItemDTO.setProductVariantResponse(productVariantResponse);










        return cartItemDTO;
    }
}
