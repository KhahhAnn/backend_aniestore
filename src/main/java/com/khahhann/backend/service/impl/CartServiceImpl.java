package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.service.CartItemService;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.ProductService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    private UserService userService;
    @Override
    public Cart creatCart(Users user) {
        Cart cart = new Cart();
        cart.setUser(user);

        return this.cartRepository.saveAndFlush(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException {
        Cart cart = this.cartRepository.findByUserId(userId);
        if(cart == null) {
            Users user = this.userService.findUserById(userId);
            cart = this.creatCart(user);
        }
        Product product = this.productService.findProductById(req.getProductId());
        CartItem isPresent = this.cartItemService.isCartItemExist(cart, product, req.getSize());
        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            double price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = this.cartItemService.createCartItem(cartItem);
            cart.getCartItem().add(createdCartItem);
        }
        return "Item add to cart";
    }

    @Override
    public Page<CartItem> findUserCart(Long userId, int pageNumber, int pageSize) {
        Cart cart = this.cartRepository.findByUserId(userId);
        List<CartItem> cartItems = cart.getCartItem();
        Collections.sort(cartItems, Comparator.comparing(CartItem::getId));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), cartItems.size());
        List<CartItem> pageContent  = cartItems.subList(startIndex, endIndex);
        Page<CartItem> pageCartItem = new PageImpl<>(pageContent, pageable, cartItems.size());
        return pageCartItem;
    }

    @Override
    public int countCartItem(Long userId) {
        Cart cart = this.cartRepository.findByUserId(userId);
        return cart.getTotalItem();
    }
}
