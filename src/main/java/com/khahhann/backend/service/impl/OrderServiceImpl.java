package com.khahhann.backend.service.impl;

import com.khahhann.backend.constant.Status;
import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.*;
import com.khahhann.backend.repository.*;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.OrderItemService;
import com.khahhann.backend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    private CartRepository cartRepository;
    @Override
    public Order createOrder(Users user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = this.addressRepository.saveAndFlush(shippingAddress);
        user.getAddressList().add(address);
        this.userRepository.saveAndFlush(user);
        Cart cart = this.cartRepository.findByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
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
        createdOrder.setOrderDate(LocalDate.now());
        createdOrder.setOrderStatus(Status.CHUA_XAC_NHAN_DON);
        createdOrder.setCreatedAt(LocalDate.now());

        Order savedOrder = this.orderRepository.saveAndFlush(createdOrder);
        for(OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            this.orderItemRepository.saveAndFlush(item);
        }
        return savedOrder;
    }

    @Override
    public Order createOrderByAddress(Users user, Address shippingAddress) {
        Cart cart = this.cartRepository.findByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();
        for(CartItem item : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setSize(item.getSize());
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
        createdOrder.setOrderDate(LocalDate.now());
        createdOrder.setOrderStatus(Status.CHUA_XAC_NHAN_DON);
        createdOrder.setCreatedAt(LocalDate.now());

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
        return this.orderRepository.getUserOrders(userId);
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.DA_XAC_NHAN_DON);
        return order;
    }

    @Override
    public Order confirmOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.DA_XAC_NHAN_DON);
        return order;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.DON_HANG_DANG_VAN_CHUYEN);
        return order;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.DON_HANG_DANG_VAN_CHUYEN);
        return order;
    }

    @Override
    public Order reciveOrderComplete(Long orderId) throws OrderException {
        Order order = this.findOrderById(orderId);
        order.setOrderStatus(Status.NHAN_HANG_THANH_CONG);
        return order;
    }

    @Override
    public List<Order> getAllOrder() {
        return this.orderRepository.findAll();
    }


    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        this.orderRepository.deleteById(orderId);

    }

    @Override
    public Order updateOrder(Order order) {
        Order exitsOrder = this.orderRepository.getReferenceById(order.getId());
        if(exitsOrder == null) {
            return null;
        }
        exitsOrder.setOrderStatus(order.getOrderStatus());
        return this.orderRepository.saveAndFlush(exitsOrder);
    }
}
