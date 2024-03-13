package com.khahhann.backend.service;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Review;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewService {
    public Review createReview(ReviewRequest req, Users user) throws ProductException;
    public List<Review> getProductsReview(Long ProductId);
}
