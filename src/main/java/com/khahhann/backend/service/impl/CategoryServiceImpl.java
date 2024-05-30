package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.Category;
import com.khahhann.backend.repository.CategoryRepository;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    @Override
    public Category updateCategory(Category category) {
        Category existCategory = this.categoryRepository.getReferenceById(category.getId());
        if(existCategory == null) {
            return null;
        }
        existCategory.setName(category.getName());

        return this.categoryRepository.saveAndFlush(existCategory);

    }

    @Override
    public ApiResponse addCategory(Category category) {
        ApiResponse apiResponse = new ApiResponse();
        if(this.categoryRepository.findByName(category.getName()) != null) {
            apiResponse.setStatus(false);
            apiResponse.setMessage("Thêm thất bại");
            return apiResponse;
        }
        this.categoryRepository.saveAndFlush(category);
        apiResponse.setStatus(true);
        apiResponse.setMessage("Thêm thành công");
        return apiResponse;
    }

    @Override
    public ApiResponse deleteCategory(Long id) {
        ApiResponse apiResponse = new ApiResponse();
        Category category = this.categoryRepository.getReferenceById(id);
        if(!this.categoryRepository.existsById(id)) {
            apiResponse.setStatus(false);
            apiResponse.setMessage("Xóa thất bại");
            return apiResponse;
        }
        category.setParentCategory(null);
        this.categoryRepository.deleteById(id);
        apiResponse.setStatus(true);
        apiResponse.setMessage("Xóa thành công");
        return apiResponse;
    }
}
