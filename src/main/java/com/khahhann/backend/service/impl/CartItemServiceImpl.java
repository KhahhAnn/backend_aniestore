package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.CartItemException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.repository.CartItemRepository;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.service.CartItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;  // Add CartRepository

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
        Cart cart = item.getCart();
        item.setQuantity(cartItem.getQuantity());
        item.setPrice(item.getQuantity() * item.getProduct().getPrice());
        item.setDiscountedPrice(item.getQuantity() * item.getProduct().getDiscountedPrice());
        this.updateCartTotals(cart);
        return this.cartItemRepository.saveAndFlush(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size) {
        return this.cartItemRepository.isCartItemExist(cart, product, size);
    }

    @Override
    @Transactional
    public void removeCartItem(Long cartItemId) throws CartItemException {
        CartItem cartItem = this.findCartItemById(cartItemId);
        Cart cart = cartItem.getCart();
        this.cartItemRepository.deleteCartItemById(cartItemId);
        this.updateCartTotals(cart);
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = this.cartItemRepository.findById(cartItemId);
        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CartItemException("CartItem not found with id - " + cartItemId);
    }

    public void updateCartTotals(Cart cart) {
        double totalPrice = 0;
        double totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem item : cart.getCartItem()) {
            totalPrice += item.getQuantity() * item.getProduct().getPrice();
            totalDiscountedPrice += item.getPrice();
            totalItem += item.getQuantity();
        }
        double discount = totalPrice - totalDiscountedPrice;
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(discount);
    }
}
