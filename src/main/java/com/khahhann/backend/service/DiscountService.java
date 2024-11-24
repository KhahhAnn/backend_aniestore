package com.khahhann.backend.service;

import com.khahhann.backend.model.Discount;
import com.khahhann.backend.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface DiscountService {
    Discount updateCategory(Discount discount);
    ApiResponse addDiscount(Discount discount);
    ApiResponse deleteDiscount(UUID id);
    Discount getDiscountByName(String discountName);

}
