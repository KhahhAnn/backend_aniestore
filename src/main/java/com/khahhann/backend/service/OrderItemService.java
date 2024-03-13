package com.khahhann.backend.service;

import com.khahhann.backend.model.OrderItem;
import org.springframework.stereotype.Service;

@Service
public interface OrderItemService {
    public OrderItem createOrderItem(OrderItem orderItem);
}
