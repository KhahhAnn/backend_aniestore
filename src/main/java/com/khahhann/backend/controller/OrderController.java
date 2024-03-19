package com.khahhann.backend.controller;

import com.khahhann.backend.exception.OrderException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Address;
import com.khahhann.backend.model.Order;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.service.OrderService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/orders")
@CrossOrigin("http://localhost:3000/")
public class OrderController {
    private OrderService orderService;
    private UserService userService;

    @PostMapping("")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.createOrder(user, shippingAddress);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException{
        Users user = this.userService.findUserProfileByJwt(jwt);
        List<Order> orderList = this.orderService.userOrderHistory(user.getId());
        return new ResponseEntity<>(orderList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable("id") Long orderId,
                                               @RequestHeader("Authorization") String jwt) throws UserException, OrderException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
