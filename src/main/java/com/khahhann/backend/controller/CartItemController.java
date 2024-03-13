package com.khahhann.backend.controller;

import com.khahhann.backend.exception.CartItemException;
import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.CartItem;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.CartItemService;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/cart-item")
public class CartItemController {
    private UserService userService;
    private CartItemService cartItemService;
    private CartService cartService;

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId,
                                                      @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        this.cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem,
                                                   @PathVariable Long cartItemId,
                                                   @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        CartItem updateCartItem = this.cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        return new ResponseEntity<>(updateCartItem, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                                     @RequestHeader("Authorization") String jwt) throws  UserException, ProductException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        this.cartService.addCartItem(user.getId(), req);
        ApiResponse res = new ApiResponse();
        res.setMessage("Item added to cart");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
