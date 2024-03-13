package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.OrderItem;
import com.khahhann.backend.repository.OrderItemRepository;
import com.khahhann.backend.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private OrderItemRepository orderItemRepository;

    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return this.orderItemRepository.saveAndFlush(orderItem);
    }
}
