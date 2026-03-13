package com.example.fashionshop.controller;

import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.repository.CartItemRepository;
import com.example.fashionshop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/cart")
//@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private CartItemService cartItemService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getCart(@PathVariable long id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(cartItemService.getCartItemById(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
