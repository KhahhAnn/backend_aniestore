package com.khahhann.backend.controller;

import com.khahhann.backend.model.Category;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/category")
public class AdminCategoryController {
    private CategoryService categoryService;
    @PostMapping()
    public ApiResponse addCategory(@RequestBody Category category) {
        return this.categoryService.addCategory(category);
    }
    @PutMapping()
    public Category updateCategory(@RequestBody Category category) {
        return this.categoryService.updateCategory(category);
    }
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable("id") Long id) {
         this.categoryService.deleteCategory(id);
    }
}
