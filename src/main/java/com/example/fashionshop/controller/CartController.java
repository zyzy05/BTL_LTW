package com.example.fashionshop.controller;

import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.CartItemService;
import com.example.fashionshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class CartController {

    @Autowired
    private CartService cartService;
    private CartItemService cartItemService;

    // hien thi gio hang theo user dang nhap
    @GetMapping("/cart")
    public ResponseEntity<ResponseData> getCart(Authentication authentication) {

        String username = authentication.getName();

        ResponseData responseData = new ResponseData();
        responseData.setData(cartService.getCartByUsername(username));

        return ResponseEntity.ok(responseData);
    }

}