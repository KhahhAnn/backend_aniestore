package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Cart;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.CartService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Data
@RestController
@RequestMapping("/api/cart")
@CrossOrigin("http://localhost:3000/")
public class CartController {
    private CartService cartService;
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Cart cart = this.cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest req,
                                              @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        this.cartService.addCartItem(user.getId(), req);
        ApiResponse res = new ApiResponse();
        res.setMessage("Products created successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse> countCartItems(@RequestHeader("Authorization") String jwt) throws UserException{
        Users user = this.userService.findUserProfileByJwt(jwt);
        int quantity = this.cartService.countCartItem(user.getId());
        ApiResponse res = new ApiResponse();
        res.setMessage(quantity + "cart item");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
