package com.example.fashionshop.dto.request.auth;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String username;

    private String email;

    private String password;

    private String fullName;

    private String phone;

    private String addressLine;

    private String city;

    private String district;

    private String ward;
}
