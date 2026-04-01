package com.example.fashionshop.controller;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // loc san pham theo muc gia
    @GetMapping("/products/filter")
    public ResponseEntity<?> filterPrice (@RequestParam Double minPrice,
                                          @RequestParam Double maxPrice)
    {

        ResponseData responseData = new ResponseData();
        responseData.setData(productService.filterPrice(minPrice,maxPrice));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // tim kiem san pham theo ten
    @GetMapping("/products/search")
    public ResponseEntity<?> filterName (@RequestParam String name )
    {

        ResponseData responseData = new ResponseData();
        responseData.setData(productService.filterName(name));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
