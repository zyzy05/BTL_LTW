package com.example.fashionshop.controller.admin;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
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
    // xoa don hang

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<?> deleteOrderById(@PathVariable long id) {

        ResponseData responseData = new ResponseData();
        responseData.setSuccess(orderService.deleteOrderById(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK) ;
    }

}
