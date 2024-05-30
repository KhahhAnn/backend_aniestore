package com.khahhann.backend.service;

import com.khahhann.backend.model.Product;
import com.khahhann.backend.request.AddProductRequest;
import org.springframework.stereotype.Service;

@Service
public interface AdminProductService {
    Product adminUpdateProduct(Product product);
    Product adminAddProduct(AddProductRequest req);
    void adminDeleteProduct(Long id);
}
