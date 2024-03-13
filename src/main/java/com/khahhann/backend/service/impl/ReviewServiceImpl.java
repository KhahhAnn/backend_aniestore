package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Review;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.ReviewRepository;
import com.khahhann.backend.request.ReviewRequest;
import com.khahhann.backend.service.ProductService;
import com.khahhann.backend.service.ReviewService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private ProductService productService;
    @Override
    public Review createReview(ReviewRequest req, Users user) throws ProductException {
        Product product = this.productService.findProductById(req.getProductId());
        Review review = new Review();
        review.setReview(req.getReview());
        review.setUser(user);
        review.setProduct(product);

        return this.reviewRepository.saveAndFlush(review);
    }

    @Override
    public List<Review> getProductsReview(Long ProductId) {
        return this.reviewRepository.getAllProductReview(ProductId);
    }
}
