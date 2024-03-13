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
@RequestMapping("/api/order")
public class OrderController {
    private OrderService orderService;
    private UserService userService;




    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress, @RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Order order = this.orderService.createOrder(user, shippingAddress);

    }
}
