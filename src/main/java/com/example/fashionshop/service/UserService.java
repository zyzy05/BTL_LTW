package com.example.fashionshop.service;


import com.example.fashionshop.dto.AddressDTO;
import com.example.fashionshop.dto.UserDTO;
import com.example.fashionshop.dto.request.user.ChangePasswordRequest;
import com.example.fashionshop.dto.request.user.UpdateUserRequest;
import com.example.fashionshop.entity.Address;
import com.example.fashionshop.entity.User;
import com.example.fashionshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    // câp nhat thong tin ca nhan va dia chi
    public boolean updateUser(long id , UpdateUserRequest request) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return false;
        }
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());

        Address address = user.getAddress();

        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setWard(request.getWard());
        address.setAddressLine(request.getAddressLine());
        user.setAddress(address);

        userRepository.save(user);

        return true;
    }

    // thay doi mat khau
    public boolean changePassword(long id, ChangePasswordRequest request) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return false;
        }

        // kiểm tra mật khẩu hiện tại
        if (!request.getCurrentPassword().equals(user.getPassword())) {
            return false;
        }

        // kiểm tra confirm password
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return false;
        }

        // đổi mật khẩu
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return true;
    }

}
