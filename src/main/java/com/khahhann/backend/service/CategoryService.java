package com.khahhann.backend.service;

import com.khahhann.backend.model.Category;
import com.khahhann.backend.response.ApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
    Category updateCategory(Category category);
    ApiResponse addCategory(Category category);
    ApiResponse deleteCategory(Long id);
}
