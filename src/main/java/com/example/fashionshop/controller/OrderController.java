package com.example.fashionshop.controller;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.repository.OrderRepository;
import com.example.fashionshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
//@PreAuthorize("hasRole('CUSTOMER')")
public class OrderController {
    @Autowired
    private OrderService orderService;

    // hien thi gio hang theo user dang nhap
    @GetMapping("/order")
    public ResponseEntity<ResponseData> getAllOrder(Authentication authentication) {

        String username = authentication.getName();

        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getAllOrdersByUsername(username));

        return ResponseEntity.ok(responseData);
    }

    // hien thi don hang theo order id
    @GetMapping("/order/{id}")
    public ResponseEntity<?> getOrderById (@PathVariable long id) {
        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getOrderItemsByIdCustomer(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
