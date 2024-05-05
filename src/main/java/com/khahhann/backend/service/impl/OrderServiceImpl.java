package com.khahhann.backend.service.impl;

import com.khahhann.backend.constant.Status;
import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.*;
import com.khahhann.backend.repository.AddressRepository;
import com.khahhann.backend.repository.OrderItemRepository;
import com.khahhann.backend.repository.OrderRepository;
import com.khahhann.backend.repository.UserRepository;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.OrderItemService;
import com.khahhann.backend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private CartService cartService;
    private AddressRepository addressRepository;
    private UserRepository userRepository;
    private OrderItemRepository orderItemRepository;
    @Override
    public Order createOrder(Users user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = this.addressRepository.saveAndFlush(shippingAddress);
        user.getAddressList().add(address);
        this.userRepository.saveAndFlush(user);
        Cart cart = this.cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());

            OrderItem createdOrderItem = this.orderItemRepository.saveAndFlush(orderItem);
            orderItems.add(createdOrderItem);
        }
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(createdOrder.getDiscount());
        createdOrder.setShippingAddress(address);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus(Status.PENDING);
        createdOrder.getPaymentDetails().setStatus(Status.PENDING);
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = this.orderRepository.saveAndFlush(createdOrder);
        for(OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            this.orderItemRepository.saveAndFlush(item);
        }
        return savedOrder;
    }

    @Override
    public Order createOrderByAddress(Users user, Address shippingAddress) {
        Cart cart = this.cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
            orderItem.setUserId(item.getUserId());
            orderItem.setDiscountedPrice(item.getDiscountedPrice());
            OrderItem createdOrderItem = this.orderItemRepository.saveAndFlush(orderItem);
            orderItems.add(createdOrderItem);
        }
        Order createdOrder = new Order();
        createdOrder.setUser(user);
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        createdOrder.setDiscount(createdOrder.getDiscount());
        createdOrder.setShippingAddress(shippingAddress);
        createdOrder.setOrderDate(LocalDateTime.now());
        createdOrder.setOrderStatus(Status.PENDING);
        createdOrder.getPaymentDetails().setStatus(Status.PENDING);
        createdOrder.setCreatedAt(LocalDateTime.now());

        Order savedOrder = this.orderRepository.saveAndFlush(createdOrder);
        for(OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            this.orderItemRepository.saveAndFlush(item);
        }
        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> opt = this.orderRepository.findById(orderId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new OrderException("Order not exist with id " + orderId);
    }

    @Override
    public List<Order> userOrderHistory(Long userId) {
        List<Order> orders = this.orderRepository.getUserOrders(userId);
        return orders;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.PLACED);
        order.getPaymentDetails().setStatus(Status.COMPLETED);
        return order;
    }

    @Override
    public Order confirmOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.CONFIRMED);
        return order;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.SHIPPED);
        return order;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.DELIVERED);
        return order;
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.CANCELLED);
        return order;
    }

    @Override
    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }


    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        this.orderRepository.deleteById(orderId);

    }
}
