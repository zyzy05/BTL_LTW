package com.example.fashionshop.controller;


import com.example.fashionshop.dto.request.user.UpdateUserRequest;
import com.example.fashionshop.dto.response.ResponseData;
import com.example.fashionshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;
    // cập nhật thông tin cá nhan
    @PutMapping("/profile/{id}")
    public ResponseEntity<?> updateUser (@RequestBody UpdateUserRequest updateProfileRequest,
                                         @PathVariable int id)
    {
        ResponseData responseData = new ResponseData();
        responseData.setSuccess(userService.updateUser(id, updateProfileRequest));
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

}
