package com.khahhann.backend.service;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.AddItemRequest;
import org.springframework.stereotype.Service;

@Service
public interface CartService {
    public Cart creatCart(Users user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException;

    public Cart findUserCart(Long userId);
}
