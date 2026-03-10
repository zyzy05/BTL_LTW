package com.example.fashionshop.service;


import com.example.fashionshop.dto.request.auth.LoginRequest;
import com.example.fashionshop.dto.request.auth.RegisterRequest;
import com.example.fashionshop.entity.Address;
import com.example.fashionshop.entity.User;
import com.example.fashionshop.entity.enums.Role;
import com.example.fashionshop.entity.enums.Status;
import com.example.fashionshop.repository.AddressRepository;
import com.example.fashionshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    // đăng kí
    public boolean register (RegisterRequest registerRequest) {
        User user = new User();

        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setPhone(registerRequest.getPhone());
        user.setRole(Role.CUSTOMER);
        user.setStatus(Status.ACTIVE);

        Address address = new Address();
        address.setAddressLine(registerRequest.getAddressLine());
        address.setCity(registerRequest.getCity());
        address.setWard(registerRequest.getWard());
        address.setDistrict(registerRequest.getDistrict());

        address.setUser(user);
        user.setAddress(address);
        addressRepository.save(address);
        userRepository.save(user);

        return true;

    }

    // đăng nhập
    public boolean login (LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return false;
        }
        if (user.getPassword().equals(loginRequest.getPassword())) {
            return true;
        }
        return false;
    }

}
