package com.khahhann.backend.repository;

import com.khahhann.backend.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin("*")
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
