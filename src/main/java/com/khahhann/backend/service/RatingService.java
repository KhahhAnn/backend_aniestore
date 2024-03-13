package com.khahhann.backend.service;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Rating;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RatingService {
    public Rating createRating(RatingRequest req, Users user) throws ProductException;
    public List<Rating> getProductsRating(Long ProductId);
}
