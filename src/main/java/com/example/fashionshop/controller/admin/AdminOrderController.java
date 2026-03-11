package com.example.fashionshop.controller.admin;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    // xem tat ca order
    @GetMapping ("/orders")
    public ResponseEntity<?> getAllOrders() {
        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getAllOrders());
        return new ResponseEntity<>(responseData, HttpStatus.OK) ;
    }
}
