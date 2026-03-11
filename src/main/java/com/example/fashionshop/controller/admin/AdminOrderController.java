package com.example.fashionshop.controller.admin;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    // xem tat ca don hang
    @GetMapping ("/orders")
    public ResponseEntity<?> getAllOrders() {
        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getAllOrders());
        return new ResponseEntity<>(responseData, HttpStatus.OK) ;
    }

    // xem chi tiet don hang theo id
    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable long id) {

        ResponseData responseData = new ResponseData();
        responseData.setData(orderService.getOrderById(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK) ;
    }
}
