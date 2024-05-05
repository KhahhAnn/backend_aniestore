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
import org.springframework.stereotype.Service;

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
        CartItem isPresent = this.cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);
            double price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = this.cartItemService.createCartItem(cartItem);
            cart.getCartItem().add(createdCartItem);
        }
        return "Item add to cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = this.cartRepository.findByUserId(userId);
        System.out.println(cart);
        double totalPrice = 0;
        double totalDiscountedPrice = 0;
        int totalItem = 0;
        for(CartItem cartItem : cart.getCartItem()) {
            totalPrice = totalPrice + cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
            totalItem = totalItem + cartItem.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setDiscount(totalDiscountedPrice - totalDiscountedPrice);

        return this.cartRepository.saveAndFlush(cart);
    }

    @Override
    public int countCartItem(Long userId) {
        Cart cart = this.findUserCart(userId);
        return cart.getTotalItem();
    }
}
