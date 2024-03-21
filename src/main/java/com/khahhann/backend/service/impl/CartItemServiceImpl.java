package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.CartItemException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartItemRepository;
import com.khahhann.backend.service.CartItemService;
import com.khahhann.backend.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private CartItemRepository cartItemRepository;
    private UserService userService;
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());
        CartItem createdCartItem = this.cartItemRepository.saveAndFlush(cartItem);
        return createdCartItem;
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = this.findCartItemById(id);
        Users user = this.userService.findUserById(item.getUserId());
        if(user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getProduct().getPrice());
            item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
        }
        return this.cartItemRepository.saveAndFlush(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = this.cartItemRepository.isCartItemExist(cart, product, size, userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem item = this.findCartItemById(cartItemId);
        Users user = this.userService.findUserById(item.getUserId());
        Users reqUser = this.userService.findUserById(userId);
        System.out.println(reqUser.getId());
            this.cartItemRepository.deleteById(cartItemId);
        if(user.getId().equals(reqUser.getId())) {
            this.cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("You can't remove another user item");
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
