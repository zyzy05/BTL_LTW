package com.example.fashionshop.service;

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
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;
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

    // them sp vao cart
    public void addToCart(String username, Long productId, Integer quantity, Long variantId) {
        User user = userRepository.findByUsername(username);
        Cart cart = cartRepository.findByUserId(user.getId());
        
        // Ensure cart exists (should be handled by getCartByUsername, but safe to check)
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }

        // Check if item with this variant already exists in the cart
        CartItem existingItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(), variantId);
        
        if (existingItem != null) {
            // Merge: just update quantity
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemRepository.save(existingItem);
        } else {
            // New Item
            Product product = productRepository.findById(productId).orElseThrow();
            ProductVariant variant = null;
            for (ProductVariant v : product.getVariants()) {
                if (v.getId() == variantId) {
                    variant = v;
                    break;
                }
            }

            if (variant == null) {
                throw new RuntimeException("Variant not found");
            }

            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProductVariant(variant);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    // xoa sp khoi cart
    public boolean deleteCartItem(long id) {

        try {
            cartItemRepository.deleteById(id);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}