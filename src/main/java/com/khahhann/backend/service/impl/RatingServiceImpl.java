package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Rating;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.repository.RatingRepository;
import com.khahhann.backend.request.RatingRequest;
import com.khahhann.backend.service.ProductService;
import com.khahhann.backend.service.RatingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {
    private RatingRepository ratingRepository;
    private ProductService productService;

    @Override
    public Rating createRating(RatingRequest req, Users user) throws ProductException {
        Product product = this.productService.findProductById(req.getProductId());
        Rating rating = new Rating();
        rating.setRating(req.getRating());
        rating.setUser(user);
        rating.setProduct(product);
        rating.setCreatedAt(LocalDateTime.now());

        return this.ratingRepository.saveAndFlush(rating);
    }

    @Override
    public List<Rating> getProductsRating(Long ProductId) {
        return this.ratingRepository.getAllProductRating(ProductId);
    }
}
