package com.example.fashionshop.dto;

import com.example.fashionshop.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private AddressDTO address;
}
