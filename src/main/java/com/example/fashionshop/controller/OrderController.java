package com.example.fashionshop.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/order")
@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {
}
