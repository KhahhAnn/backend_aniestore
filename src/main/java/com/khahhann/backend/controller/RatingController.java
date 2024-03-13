package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Rating;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.RatingRequest;
import com.khahhann.backend.service.RatingService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    private UserService userService;
    private RatingService ratingService;

    @PostMapping("/create")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Rating rating = this.ratingService.createRating(req, user);
        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Rating>> getProductRatings(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long productId) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        List<Rating> ratingList = this.ratingService.getProductsRating(productId);
        return new ResponseEntity<>(ratingList, HttpStatus.OK);
    }
}
