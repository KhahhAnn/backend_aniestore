package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Order;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.OrderService;
import com.khahhann.backend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private CartRepository cartRepository;
    private CartService cartService;
    private ProductService productService;
    @Override
    public Order createOrder(Users user, Address shippingAddress) {
        return null;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrder() {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
