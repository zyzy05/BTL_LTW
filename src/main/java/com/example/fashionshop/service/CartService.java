package com.example.fashionshop.service;

import com.example.fashionshop.dto.CartDTO;
import com.example.fashionshop.dto.CartItemDTO;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.entity.*;
import com.example.fashionshop.repository.CartItemRepository;
import com.example.fashionshop.repository.CartRepository;
import com.example.fashionshop.repository.ProductRepository;
import com.example.fashionshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // hien thi cart theo user id
    private CartDTO convertToDTO(Cart cart) {

        CartDTO cartDTO = new CartDTO();

        List<CartItem> cartItemList = cartItemRepository.findByCartId(cart.getId());
        List<CartItemDTO> cartItemDTOs = new ArrayList<>();
        for (CartItem Item : cartItemList) {
            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setQuantity(Item.getQuantity());

            // product variant
            ProductVariant productVariant = Item.getProductVariant();
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();

            // product
            Product product = productVariant.getProduct();
            ProductsResponse productsResponse = new ProductsResponse();
            productsResponse.setName(product.getName());
            productsResponse.setPrice(product.getPrice());

            // images
            List<ProductImage> productImageList = product.getImages();
            List<ProductImageResponse> productImageResponseList = new ArrayList<>();
            for (ProductImage productImage : productImageList) {
                ProductImageResponse productImageResponse = new ProductImageResponse();
                productImageResponse.setImageUrl(productImage.getImageUrl());
                productImageResponseList.add(productImageResponse);
            }
            productsResponse.setImages(productImageResponseList);

            productVariantResponse.setProductResponse(productsResponse);

            cartItemDTO.setProductVariantResponse(productVariantResponse);

            cartItemDTOs.add(cartItemDTO);
        }
        cartDTO.setItems(cartItemDTOs);
        return cartDTO;
    }

    // lay user id tu nguoi dung dang nhap
    public CartDTO getCartByUsername(String username) {

        User user = userRepository.findByUsername(username);

        Cart cart = cartRepository.findByUserId(user.getId());

        return convertToDTO(cart);
    }
}