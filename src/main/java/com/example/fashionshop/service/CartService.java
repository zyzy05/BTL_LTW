package com.example.fashionshop.service;

import com.example.fashionshop.dto.CartDTO;
import com.example.fashionshop.dto.CartItemDTO;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
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
            cartItemDTO.setId(Math.toIntExact(Item.getId()));
            cartItemDTO.setQuantity(Item.getQuantity());

            // product variant
            ProductVariant productVariant = Item.getProductVariant();
            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
            productVariantResponse.setId(productVariant.getId());

            // product
            Product product = productVariant.getProduct();
            ProductsResponse productsResponse = new ProductsResponse();
            productsResponse.setId(product.getId());
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

            // size
            Size size = productVariant.getSize();
            if (size != null) {
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());
                productVariantResponse.setSize(sizeResponse);
            }

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
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        return convertToDTO(cart);
    }
}