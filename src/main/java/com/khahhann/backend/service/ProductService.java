package com.khahhann.backend.service;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.request.CreateProductRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public Product createProduct(CreateProductRequest productRequest);
    public String deleteProduct(Long productId) throws ProductException;
    public Product updateProduct(Long productId, Product req) throws ProductException;
    public Product findProductById(Long id) throws ProductException;
    public List<Product> findProductByCategory(String category);
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes,
                                       Integer minPrice, Integer maxPrice, Integer minDiscount,
                                       String sort, String stock, Integer pageNumber, Integer pageSize);
    public List<Product> findAllProducts();

}
