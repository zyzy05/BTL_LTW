package com.example.fashionshop.controller.admin;


import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;
    // lay full du lieu user
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {

        ResponseData responseData = new ResponseData();
        responseData.setData(userService.getAllUsers());
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    // xoa user

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(userService.deleteUser(id));
        return new ResponseEntity<>(responseData, HttpStatus.OK);

    }
}
