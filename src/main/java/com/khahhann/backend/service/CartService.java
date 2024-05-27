package com.khahhann.backend.service;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.response.CartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface CartService {
    public Cart creatCart(Users user);

    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException;

    public CartResponse findUserCart(Long userId, int pageNumber, int pageSize);

    public int countCartItem(Long userId);
    void updateCartTotals(Long cartId);
}
