package com.example.fashionshop.service;


import com.example.fashionshop.dto.*;
import com.example.fashionshop.dto.response.products.ProductImageResponse;
import com.example.fashionshop.dto.response.products.ProductVariantResponse;
import com.example.fashionshop.dto.response.products.ProductsResponse;
import com.example.fashionshop.dto.response.products.SizeResponse;
import com.example.fashionshop.entity.*;
import com.example.fashionshop.entity.enums.OrderStatus;
import com.example.fashionshop.entity.enums.PaymentMethod;
import com.example.fashionshop.entity.enums.PaymentStatus;
import com.example.fashionshop.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    // ================= ADMIN =================

    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> request = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {

            if (order == null) continue;

            OrderDTO orderDTO = new OrderDTO();
            orderDTO.setId(order.getId());
            orderDTO.setTotalPrice(order.getTotalPrice());
            orderDTO.setStatus(order.getStatus());
            orderDTO.setPaymentMethod(order.getPaymentMethod());
            orderDTO.setPaymentStatus(order.getPaymentStatus());
            orderDTO.setShippingName(order.getShippingName());
            orderDTO.setShippingAddress(order.getShippingAddress());
            orderDTO.setCreatedAt(order.getCreatedAt());

            // USER
            User user = order.getUser();
            if (user != null) {
                UserDTO userDTO = new UserDTO();
                userDTO.setFullName(user.getFullName());
                userDTO.setEmail(user.getEmail());
                userDTO.setPhone(user.getPhone());

                Address address = user.getAddress();
                if (address != null) {
                    AddressDTO addressDTO = new AddressDTO();
                    addressDTO.setWard(address.getWard());
                    addressDTO.setCity(address.getCity());
                    addressDTO.setDistrict(address.getDistrict());
                    addressDTO.setAddressLine(address.getAddressLine());
                    userDTO.setAddress(addressDTO);
                }

                orderDTO.setUser(userDTO);
            }

            // ITEMS
            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
            if (order.getItems() != null) {
                for (OrderItem orderItem : order.getItems()) {

                    OrderItemDTO dto = new OrderItemDTO();
                    dto.setQuantity(orderItem.getQuantity());
                    dto.setPrice(orderItem.getPrice());

                    ProductVariant variant = orderItem.getProductVariant();
                    if (variant != null) {
                        Size size = variant.getSize();
                        if (size != null) {
                            SizeResponse sizeRes = new SizeResponse();
                            sizeRes.setName(size.getName());

                            ProductVariantResponse variantRes = new ProductVariantResponse();
                            variantRes.setSize(sizeRes);

                            dto.setVariant(variantRes);
                        }
                    }

                    orderItemDTOS.add(dto);
                }
            }

            orderDTO.setOrderItems(orderItemDTOS);
            request.add(orderDTO);
        }

        return request;
    }

    public OrderDTO getOrderById(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;

        OrderDTO orderDTO = new OrderDTO();

        orderDTO.setId(order.getId());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        orderDTO.setShippingName(order.getShippingName());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setCreatedAt(order.getCreatedAt());

        // USER
        User user = order.getUser();
        if (user != null) {
            UserDTO userDTO = new UserDTO();
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setPhone(user.getPhone());

            Address address = user.getAddress();
            if (address != null) {
                AddressDTO addressDTO = new AddressDTO();
                addressDTO.setWard(address.getWard());
                addressDTO.setCity(address.getCity());
                addressDTO.setDistrict(address.getDistrict());
                addressDTO.setAddressLine(address.getAddressLine());
                userDTO.setAddress(addressDTO);
            }

            orderDTO.setUser(userDTO);
        }

        // ITEMS
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {

                OrderItemDTO dto = new OrderItemDTO();
                dto.setQuantity(item.getQuantity());
                dto.setPrice(item.getPrice());

                orderItemDTOS.add(dto);
            }
        }

        orderDTO.setOrderItems(orderItemDTOS);

        return orderDTO;
    }

    // ================= CUSTOMER =================

    public List<OrderItemDTO> getOrderItemsByIdCustomer(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return null;

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setPaymentMethod(order.getPaymentMethod());
        orderDTO.setPaymentStatus(order.getPaymentStatus());
        orderDTO.setShippingName(order.getShippingName());

        List<OrderItemDTO> itemDTOList = new ArrayList<>();

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        for (OrderItem item : orderItems) {

            OrderItemDTO dto = new OrderItemDTO();
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());

            itemDTOList.add(dto);
        }

        orderDTO.setOrderItems(itemDTOList);

        return itemDTOList;
    }

    public List<OrderDTO> getAllOrdersByUsername(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) return new ArrayList<>();

        List<Order> orders = orderRepository.findByUserId(user.getId());
        List<OrderDTO> result = new ArrayList<>();

        for (Order order : orders) {

            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setTotalPrice(order.getTotalPrice());
            dto.setStatus(order.getStatus());
            dto.setPaymentMethod(order.getPaymentMethod());
            dto.setPaymentStatus(order.getPaymentStatus());
            dto.setShippingName(order.getShippingName());

            result.add(dto);
        }

        return result;
    }

    // ================= ORDER ACTION =================

    @Transactional
    public boolean placeOrder(String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) return false;

        Cart cart = cartRepository.findByUser(user);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return false;
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.COD);
        order.setShippingName(user.getFullName());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0;

        for (CartItem cartItem : cart.getItems()) {

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductVariant(cartItem.getProductVariant());
            item.setQuantity(cartItem.getQuantity());

            ProductVariant variant = cartItem.getProductVariant();
            if (variant == null) continue;

            Product product = variant.getProduct();
            if (product == null) continue;

            double price = product.getPrice();

            item.setProductVariant(variant);
            item.setPrice(price);

            totalPrice += price * cartItem.getQuantity();

            orderItems.add(item);
        }

        order.setItems(orderItems);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return true;
    }

    public boolean cancelOrder(long id) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return false;

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return true;
    }

    public boolean updateStatusOrder(long id, String status) {

        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) return false;

        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (Exception e) {
            return false;
        }

        orderRepository.save(order);
        return true;
    }
}

//@Service
//public class OrderService {
//    @Autowired
//    private OrderRepository orderRepository;
//
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    // xem tat ca don hang - Admin
//    public List<OrderDTO> getAllOrders() {
//        List<OrderDTO> request = new ArrayList<>();
//        List<Order> orders = orderRepository.findAll();
//        for (Order order : orders) {
//
//            OrderDTO orderDTO = new OrderDTO();
//            orderDTO.setId(order.getId());
//            orderDTO.setTotalPrice(order.getTotalPrice());
//            orderDTO.setStatus(order.getStatus());
//            orderDTO.setPaymentMethod(order.getPaymentMethod());
//            orderDTO.setPaymentStatus(order.getPaymentStatus());
//            orderDTO.setShippingName(order.getShippingName());
//            orderDTO.setShippingAddress(order.getShippingAddress());
//            orderDTO.setCreatedAt(order.getCreatedAt());
//
//            User user = order.getUser();
//
//            UserDTO userDTO = new UserDTO();
//
//            userDTO.setFullName(user.getUsername());
//            userDTO.setEmail(user.getEmail());
//            userDTO.setPhone(user.getPhone());
//
//            Address address = user.getAddress();
//            AddressDTO addressDTO = new AddressDTO();
//
//            addressDTO.setWard(address.getWard());
//            addressDTO.setCity(address.getCity());
//            addressDTO.setDistrict(address.getDistrict());
//            addressDTO.setAddressLine(address.getAddressLine());
//
//            userDTO.setAddress(addressDTO);
//
//            orderDTO.setUser(userDTO);
//
//            List<OrderItem> orderItems = order.getItems();
//            List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
//            for (OrderItem orderItem : orderItems) {
//                OrderItemDTO orderItemDTO = new OrderItemDTO();
//
//                orderItemDTO.setQuantity(orderItem.getQuantity());
//                orderItemDTO.setPrice(orderItem.getPrice());
//
//                ProductVariant productVariant = orderItem.getProductVariant();
//                Size size = productVariant.getSize();
//                SizeResponse sizeResponse = new SizeResponse();
//                sizeResponse.setName(size.getName());
//                ProductVariantResponse productVariantResponse = new ProductVariantResponse();
//                productVariantResponse.setSize(sizeResponse);
//
//                orderItemDTO.setVariant(productVariantResponse);
//
//
//                orderItemDTOS.add(orderItemDTO);
//            }
//            request.add(orderDTO);
//        }
//
//        return request;
//    }
//
//    // xem chi tiet don hang theo id - Admin
//    public OrderDTO getOrderById(long id) {
//        OrderDTO orderDTO = new OrderDTO();
//        Order order = orderRepository.findById(id).orElse(null);
//        orderDTO.setId(order.getId());
//        orderDTO.setTotalPrice(order.getTotalPrice());
//        orderDTO.setStatus(order.getStatus());
//        orderDTO.setPaymentMethod(order.getPaymentMethod());
//        orderDTO.setPaymentStatus(order.getPaymentStatus());
//        orderDTO.setShippingName(order.getShippingName());
//        orderDTO.setShippingAddress(order.getShippingAddress());
//        orderDTO.setCreatedAt(order.getCreatedAt());
//
//        User user = order.getUser();
//
//        UserDTO userDTO = new UserDTO();
//
//        userDTO.setFullName(user.getUsername());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPhone(user.getPhone());
//
//        Address address = user.getAddress();
//        AddressDTO addressDTO = new AddressDTO();
//
//        addressDTO.setWard(address.getWard());
//        addressDTO.setCity(address.getCity());
//        addressDTO.setDistrict(address.getDistrict());
//        addressDTO.setAddressLine(address.getAddressLine());
//
//        userDTO.setAddress(addressDTO);
//
//        orderDTO.setUser(userDTO);
//
//        List<OrderItem> orderItems = order.getItems();
//        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
//        for (OrderItem orderItem : orderItems) {
//            OrderItemDTO orderItemDTO = new OrderItemDTO();
//
//            orderItemDTO.setQuantity(orderItem.getQuantity());
//            orderItemDTO.setPrice(orderItem.getPrice());
//
//            ProductVariant productVariant = orderItem.getProductVariant();
//            Size size = productVariant.getSize();
//            SizeResponse sizeResponse = new SizeResponse();
//            sizeResponse.setName(size.getName());
//            ProductVariantResponse productVariantResponse = new ProductVariantResponse();
//            productVariantResponse.setSize(sizeResponse);
//
//            orderItemDTO.setVariant(productVariantResponse);
//
//
//            orderItemDTOS.add(orderItemDTO);
//        }
//        return orderDTO;
//    }
//
//    // xem chi tiet don hang theo Id - Customer
//    public OrderDTO getOrderItemsByIdCustomer(long id) {
//        Order order = orderRepository.findById(id).orElse(null);
//
//        if (order == null) {
//            return null;
//        }
//
//        OrderDTO orderDTO = new OrderDTO();
//
//        orderDTO.setTotalPrice(order.getTotalPrice());
//        orderDTO.setPaymentMethod(order.getPaymentMethod());
//        orderDTO.setPaymentStatus(order.getPaymentStatus());
//        orderDTO.setShippingName(order.getShippingName());
//
//        User user = order.getUser();
//        UserDTO userDTO = new UserDTO();
//
//        Address address = user.getAddress();
//        AddressDTO addressDTO = new AddressDTO();
//
//        addressDTO.setWard(address.getWard());
//        addressDTO.setCity(address.getCity());
//        addressDTO.setDistrict(address.getDistrict());
//        addressDTO.setAddressLine(address.getAddressLine());
//
//        userDTO.setAddress(addressDTO);
//        orderDTO.setUser(userDTO);
//
//        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
//        List<OrderItemDTO> itemDTOList = new ArrayList<>();
//
//        for (OrderItem item : orderItems) {
//
//            OrderItemDTO itemDTO = new OrderItemDTO();
//            itemDTO.setQuantity(item.getQuantity());
//            itemDTO.setPrice(item.getPrice());
//
//            ProductVariant variant = item.getProductVariant();
//            ProductVariantResponse variantRes = new ProductVariantResponse();
//
//            Product product = variant.getProduct();
//            ProductsResponse productRes = new ProductsResponse();
//            productRes.setName(product.getName());
//            productRes.setPrice(product.getPrice());
//
//            // images
//            List<ProductImageResponse> imgList = new ArrayList<>();
//            for (ProductImage img : product.getImages()) {
//                ProductImageResponse imgRes = new ProductImageResponse();
//                imgRes.setImageUrl(img.getImageUrl());
//                imgList.add(imgRes);
//            }
//            productRes.setImages(imgList);
//
//            variantRes.setProductResponse(productRes);
//            itemDTO.setVariant(variantRes);
//
//            itemDTOList.add(itemDTO);
//        }
//
//        orderDTO.setOrderItems(itemDTOList);
//
//        return orderDTO;
//    }
//
//    // xem tat ca don hang theo id - Customer
//    public List<OrderDTO> getAllOrdersByUsername(String username) {
//
//        User user = userRepository.findByUsername(username);
//        if (user == null) return new ArrayList<>();
//
//        List<Order> orders = orderRepository.findByUserId(user.getId());
//        List<OrderDTO> orderDTOList = new ArrayList<>();
//
//        for (Order order : orders) {
//
//            OrderDTO orderDTO = new OrderDTO();
//
//            orderDTO.setTotalPrice(order.getTotalPrice());
//            orderDTO.setPaymentMethod(order.getPaymentMethod());
//            orderDTO.setPaymentStatus(order.getPaymentStatus());
//            orderDTO.setShippingName(order.getShippingName());
//
//            // user info
//            UserDTO userDTO = new UserDTO();
//            Address address = user.getAddress();
//
//            if (address != null) {
//                AddressDTO addressDTO = new AddressDTO();
//                addressDTO.setWard(address.getWard());
//                addressDTO.setCity(address.getCity());
//                addressDTO.setDistrict(address.getDistrict());
//                addressDTO.setAddressLine(address.getAddressLine());
//                userDTO.setAddress(addressDTO);
//            }
//
//            orderDTO.setUser(userDTO);
//
//            orderDTOList.add(orderDTO);
//        }
//
//        return orderDTOList;
//    }
//
//    // dat hang
//    @Transactional
//    public boolean placeOrder(String username) {
//
//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            return false;
//        }
//
//        Cart cart = cartRepository.findByUser(user);
//        if (cart == null || cart.getItems().isEmpty()) {
//            return false;
//        }
//
//        Order order = new Order();
//        order.setUser(user);
//        order.setStatus(OrderStatus.PENDING);
//        order.setPaymentStatus(PaymentStatus.PENDING);
//        order.setPaymentMethod(PaymentMethod.COD);
//        order.setShippingName(user.getFullName());
//
//        List<OrderItem> orderItems = new ArrayList<>();
//        double totalPrice = 0;
//
//        for (CartItem cartItem : cart.getItems()) {
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProductVariant(cartItem.getProductVariant());
//            orderItem.setQuantity(cartItem.getQuantity());
//
//            ProductVariant variant = orderItem.getProductVariant();
//            ProductVariantResponse variantRes = new ProductVariantResponse();
//
//            Product product = variant.getProduct();
//            ProductsResponse productRes = new ProductsResponse();
//
//            totalPrice += product.getPrice() * cartItem.getQuantity();
//
//            orderItems.add(orderItem);
//        }
//
//        order.setItems(orderItems);
//        order.setTotalPrice(totalPrice);
//
//        orderRepository.save(order);
//
//        // clear cart
//        cart.getItems().clear();
//        cartRepository.save(cart);
//
//        return true;
//    }
//
//    // huy don hang
//    public boolean cancelOrder(long id) {
//
//        Order order = orderRepository.findById(id).orElse(null);
//
//        if(order == null){
//            return false;
//        }
//
//        order.setStatus(OrderStatus.CANCELLED);
//        orderRepository.save(order);
//
//        return true;
//    }
//
//    // cap nhat trang thai don - Admin
//    public boolean updateStatusOrder(long id, String status) {
//
//        Order order = orderRepository.findById(id).orElse(null);
//
//        if(order == null){
//            return false;
//        }
//
//        order.setStatus(OrderStatus.valueOf(status));
//        orderRepository.save(order);
//
//        return true;
//    }
//}
