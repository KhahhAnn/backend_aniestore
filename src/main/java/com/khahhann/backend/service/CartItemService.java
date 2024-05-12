package com.khahhann.backend.service;

import com.khahhann.backend.exception.CartItemException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {
    public CartItem createCartItem(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
    public CartItem isCartItemExist(Cart cart, Product product, String size);
    public void removeCartItem(Long cartItemId) throws CartItemException;
    public CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
