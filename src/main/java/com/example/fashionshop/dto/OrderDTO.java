package com.example.fashionshop.dto;

import com.example.fashionshop.entity.enums.OrderStatus;
import com.example.fashionshop.entity.enums.PaymentMethod;
import com.example.fashionshop.entity.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    private Integer id;
    private Double totalPrice;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String shippingName;
    private String shippingPhone;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private UserDTO user;
    private List<OrderItemDTO> orderItems;

}
