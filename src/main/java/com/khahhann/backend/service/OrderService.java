package com.khahhann.backend.service;

import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Order;
import com.khahhann.backend.model.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
     Order createOrder(Users user, Address shippingAddress);
     Order createOrderByAddress(Users user, Address shippingAddress);
     Order findOrderById(Long orderId) throws OrderException;
     List<Order> userOrderHistory(Long userId);
     Order placedOrder(Long orderId) throws OrderException;
     Order confirmOrder(Long orderId) throws OrderException;
     Order shippedOrder(Long orderId) throws OrderException;
     Order deliveredOrder(Long orderId) throws OrderException;
     Order reciveOrderComplete(Long orderId) throws OrderException;
     List<Order> getAllOrder();
     void deleteOrder(Long orderId) throws OrderException;
     Order updateOrder(Order order);
}
