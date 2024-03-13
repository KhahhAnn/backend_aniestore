package com.khahhann.backend.service;

import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Order;
import com.khahhann.backend.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    public Order createOrder(Users user, Address shippingAddress);
    public Order findOrderById(Long orderId) throws OrderException;
    public List<Order> userOrderHistory(Long userId);
    public Order placedOrder(Long orderId) throws OrderException;
    public Order confirmOrder(Long orderId) throws OrderException;
    public Order shippedOrder(Long orderId) throws OrderException;
    public Order deliveredOrder(Long orderId) throws OrderException;
    public Order cancelOrder(Long orderId) throws OrderException;
    public List<Order> getAllOrder();
    public void deleteOrder(Long orderId) throws OrderException;


}
