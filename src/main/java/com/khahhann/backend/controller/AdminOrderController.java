package com.khahhann.backend.controller;

import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.model.Order;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {
    private OrderService orderService;
    @GetMapping("")
    public ResponseEntity<List<Order>> getAllOrdersHandle() {
        List<Order> orders = this.orderService.getAllOrder();
        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }
    @PutMapping("/{orderId}/confirmed")
    public ResponseEntity<Order> confirmedOrderHandle(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = this.orderService.confirmOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @PutMapping("/{orderId}/ship")
    public ResponseEntity<Order> shippedOrderHandle(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = this.orderService.shippedOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
    @PutMapping("/{orderId}/deliver")
    public ResponseEntity<Order> deliverOrderHandle(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        Order order = this.orderService.deliveredOrder(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandle(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws OrderException {
        this.orderService.deleteOrder(orderId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Order deleted successfully");
        res.setStatus(true);
        return new ResponseEntity<ApiResponse>(res, HttpStatus.OK);
    }
    @PutMapping()
    public Order updateOrder(@RequestBody Order order) {
        return this.orderService.updateOrder(order);
    }
}
