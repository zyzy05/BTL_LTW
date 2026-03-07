package com.example.fashionshop.controller;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    // hiển thi toàn bộ sản phẩm
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        ResponseData responseData = new ResponseData();
        responseData.setData(productService.getAllProducts());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // hiển thị chi tiết san pham

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetail (@PathVariable long id)
    {

        ResponseData responseData = new ResponseData();
        responseData.setData(productService.getProductDetail(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }


}
