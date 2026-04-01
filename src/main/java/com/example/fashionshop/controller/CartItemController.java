package com.example.fashionshop.controller;

import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.CartItemService;
import com.example.fashionshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/cart/items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CartService cartService;

    // thêm sản phẩm vào giỏ
    @PostMapping
    public ResponseEntity<ResponseData> addToCart(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam Long variantId,
            @RequestParam Integer quantity
    ) {
        ResponseData responseData = new ResponseData();

        try {
            String username = authentication.getName();
            cartItemService.addToCart(username, productId, quantity, variantId);

            responseData.setData("Add to cart success");
            return ResponseEntity.ok(responseData);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(responseData);
        }
    }

    // xem chi tiết 1 sản phẩm trong giỏ
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData> getCartItem(@PathVariable Long id) {

        ResponseData responseData = new ResponseData();
        responseData.setData(cartItemService.getCartItemById(id));

        return ResponseEntity.ok(responseData);
    }

    // xoá sản phẩm khỏi giỏ
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData> deleteCartItem(@PathVariable Long id) {

        boolean deleted = cartItemService.deleteCartItem(id);

        ResponseData responseData = new ResponseData();
        responseData.setData(deleted);

        return ResponseEntity.ok(responseData);
    }
}