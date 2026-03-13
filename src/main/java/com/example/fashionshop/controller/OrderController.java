package com.example.fashionshop.controller;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.repository.OrderRepository;
import com.example.fashionshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/order")
//@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById (@PathVariable long id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getOrderByIdCustomer(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}
