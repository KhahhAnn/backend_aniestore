package com.khahhann.backend.service.impl;

import com.khahhann.backend.model.Category;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.repository.CategoryRepository;
import com.khahhann.backend.repository.ProductRepository;
import com.khahhann.backend.request.AddProductRequest;
import com.khahhann.backend.service.AdminProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminProductServiceImpl implements AdminProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    @Override
    public Product adminUpdateProduct(Product product) {
        Product existProduct = this.productRepository.getReferenceById(product.getId());
        if(existProduct == null) {
            return null;
        }
        existProduct.setQuantity(product.getQuantity());
        existProduct.setPrice(product.getPrice());
        existProduct.setColor(product.getColor());
        existProduct.setTitle(product.getTitle());
        existProduct.setDescription(product.getDescription());
        existProduct.setImageUrl(product.getImageUrl());
        existProduct.setBrand(product.getBrand());
        existProduct.setPrice(product.getPrice());
        existProduct.setDiscountedPrice(product.getDiscountedPrice());
        return this.productRepository.saveAndFlush(existProduct);
    }

    @Override
    public Product adminAddProduct(AddProductRequest req) {
        Product product = new Product();

        Category category = this.categoryRepository.findById(req.getCategoryId()).orElse(null);
        if (category != null) {
            product.setCategory(category);
            product.setTitle(req.getTitle());
            product.setBrand(req.getBrand());
            product.setColor(req.getColor());
            product.setImageUrl(req.getImageUrl());
            product.setPrice(req.getPrice());
            product.setDiscountedPrice(req.getDiscountedPrice());
            product.setQuantity(req.getQuantity());
            return this.productRepository.saveAndFlush(product);
        } else {
            throw new IllegalArgumentException("Category not found for ID: " + req.getCategoryId());
        }
    }

    @Override
    public void adminDeleteProduct(Long id) {
        this.productRepository.deleteById(id);
    }
}
