package com.example.fashionshop.service;


import com.example.fashionshop.dto.AddressDTO;
import com.example.fashionshop.dto.OrderDTO;
import com.example.fashionshop.dto.OrderItemDTO;
import com.example.fashionshop.dto.UserDTO;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.entity.*;
import com.example.fashionshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    // xem tat ca don hang
    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> request = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setTotalPrice(order.getTotalPrice());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setPaymentMethod(order.getPaymentMethod());
            orderDTO.setPaymentStatus(order.getPaymentStatus());
            orderDTO.setShippingName(order.getShippingName());
            orderDTO.setShippingAddress(order.getShippingAddress());
            orderDTO.setCreatedAt(order.getCreatedAt());

            User user = order.getUser();

            UserDTO userDTO = new UserDTO();

            userDTO.setFullName(user.getUsername());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());

            Address address = user.getAddress();
            AddressDTO addressDTO = new AddressDTO();

            addressDTO.setWard(address.getWard());
            addressDTO.setCity(address.getCity());
            addressDTO.setAddressLine(address.getAddressLine());

            userDTO.setAddress(addressDTO);

            orderDTO.setUser(userDTO);

            List<OrderItem> orderItems = order.getItems();
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            for(OrderItem orderItem : orderItems) {
                OrderItemDTO orderItemDTO = new OrderItemDTO();

                orderItemDTO.setQuantity(orderItem.getQuantity());
                orderItemDTO.setPrice(orderItem.getPrice());

                ProductVariant productVariant = orderItem.getProductVariant();
                Size size = productVariant.getSize() ;
                SizeResponse sizeResponse = new SizeResponse();
                sizeResponse.setName(size.getName());
                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
                productVariantResponse.setSize(sizeResponse);

                orderItemDTO.setVariant(productVariantResponse);


                orderItemDTOS.add(orderItemDTO);
            }
            request.add(orderDTO);
        }

        return request;
    }
}
