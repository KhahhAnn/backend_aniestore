package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.CartRepository;
import com.khahhann.backend.request.AddItemRequest;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.response.CartResponse;
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
    private CartRepository cartRepository;

    @GetMapping("")
    public ResponseEntity<CartResponse> findUserCart(@RequestHeader("Authorization") String jwt,
                                                     @RequestParam(value = "page", required = false) Integer pageNumber,
                                                     @RequestParam(value = "pageSize", required = false) Integer pageSize)
            throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        CartResponse res = this.cartService.findUserCart(user.getId(), pageNumber, pageSize);
        return new ResponseEntity<>(res, HttpStatus.OK);
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
    public ResponseEntity<Integer> countCartItems(@RequestHeader("Authorization") String jwt) throws UserException{
        Users user = this.userService.findUserProfileByJwt(jwt);
        int quantity = this.cartService.countCartItem(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(quantity);
    }

    @GetMapping("/update-cart-total")
    public ResponseEntity<String> updateCart(@RequestHeader("Authorization") String jwt) throws UserException{
        Users user = this.userService.findUserProfileByJwt(jwt);
        this.cartService.updateCartTotals(this.cartRepository.findByUserId(user.getId()).getId());
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

}
