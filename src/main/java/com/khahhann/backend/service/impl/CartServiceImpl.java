package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartItemRepository;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.response.CartResponse;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;
    private UserService userService;
    private CartItemRepository cartItemRepository;
    @Override
    public Cart creatCart(Users user) {
        Cart cart = new Cart();
        cart.setUser(user);

        return this.cartRepository.saveAndFlush(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException, UserException {
        Cart cart = this.cartRepository.findByUserId(userId);
        if (cart == null) {
            Users user = this.userService.findUserById(userId);
            cart = this.creatCart(user);
        }
        Product product = this.productService.findProductById(req.getProductId());
        CartItem existingCartItem = this.cartItemService.isCartItemExist(cart, product, req.getSize());

        if (existingCartItem == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            double price = req.getQuantity() * product.getDiscountedPrice();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());
            CartItem createdCartItem = this.cartItemService.createCartItem(cartItem);
            cart.getCartItem().add(createdCartItem);
        } else {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            double price = existingCartItem.getQuantity() * product.getDiscountedPrice();
            existingCartItem.setPrice(price);
            this.cartItemRepository.saveAndFlush(existingCartItem);
        }
        updateCartTotals(cart.getId());
        this.cartRepository.saveAndFlush(cart);
        return "Item added to cart";
    }


    @Override
    public CartResponse findUserCart(Long userId, int pageNumber, int pageSize) {
        CartResponse cartResponse = new CartResponse();
        Cart cart = this.cartRepository.findByUserId(userId);
        List<CartItem> cartItems = cart.getCartItem();
        Collections.sort(cartItems, Comparator.comparing(CartItem::getId));
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), cartItems.size());
        List<CartItem> pageContent  = cartItems.subList(startIndex, endIndex);
        Page<CartItem> pageCartItem = new PageImpl<>(pageContent, pageable, cartItems.size());
        cartResponse.setCartItems(pageCartItem);
        cartResponse.setCart(cart);
        return cartResponse;
    }

    @Override
    public int countCartItem(Long userId) {
        Cart cart = this.cartRepository.findByUserId(userId);
        return this.cartItemRepository.countCartItemByCartId(cart.getId());
    }

    @Override
    public void updateCartTotals(Long cartId) {
        Cart cart = this.cartRepository.getReferenceById(cartId);
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
        this.cartRepository.saveAndFlush(cart);
    }
}
