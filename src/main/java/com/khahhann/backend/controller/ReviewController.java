package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.exception.UserException;
import com.khahhann.backend.model.Rating;
import com.khahhann.backend.model.Review;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.RatingRequest;
import com.khahhann.backend.request.ReviewRequest;
import com.khahhann.backend.service.ReviewService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Review> createRating(@RequestBody ReviewRequest req,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        Review review = this.reviewService.createReview(req, user);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductRatings(@RequestHeader("Authorization") String jwt,
                                                          @PathVariable Long productId) throws UserException {
        Users user = this.userService.findUserProfileByJwt(jwt);
        List<Review> ratingList = this.reviewService.getProductsReview(productId);
        return new ResponseEntity<>(ratingList, HttpStatus.OK);
    }
}
