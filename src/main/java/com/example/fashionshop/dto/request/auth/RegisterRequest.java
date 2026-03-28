package com.example.fashionshop.dto.request.auth;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String fullName;
    private String phone;
    private String addressLine;
    private String city;
    private String district;
    private String ward;
}
