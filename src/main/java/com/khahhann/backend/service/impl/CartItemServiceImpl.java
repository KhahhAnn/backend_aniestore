package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.CartItemException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.repository.CartItemRepository;
import com.khahhann.backend.service.CartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private CartItemRepository cartItemRepository;
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        return this.cartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = this.findCartItemById(id);
        item.setQuantity(cartItem.getQuantity());
        item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
        return this.cartItemRepository.saveAndFlush(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size) {
        return this.cartItemRepository.isCartItemExist(cart, product, size);
    }

    @Transactional
    @Override
    public void removeCartItem(Long cartItemId) throws CartItemException {
          CartItem cartItem = this.findCartItemById(cartItemId);
        if (cartItem != null) {
            this.cartItemRepository.deleteCartItemById(cartItemId);
        } else {
            throw new CartItemException("Cart item not found");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = this.cartItemRepository.findById(cartItemId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("CartItem not fount with id - " + cartItemId);
    }


}
