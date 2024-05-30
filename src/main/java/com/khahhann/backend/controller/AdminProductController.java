package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.model.Users;
import com.khahhann.backend.request.AddProductRequest;
import com.khahhann.backend.request.CreateProductRequest;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.AdminProductService;
import com.khahhann.backend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {
    private ProductService productService;
    private AdminProductService adminProductService;

    @PostMapping("/")
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest req) {
        Product product = this.productService.createProduct(req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    @DeleteMapping("/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) throws ProductException {
        this.productService.deleteProduct(productId);
        ApiResponse res = new ApiResponse();
        res.setMessage("Product deleted successfull");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> findAllProduct() {
        List<Product> products = this.productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product req, @PathVariable Long productId) throws ProductException {
        Product product = this.productService.updateProduct(productId, req);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PostMapping("/creates")
    public ResponseEntity<ApiResponse> createMultipeProduct(@RequestBody CreateProductRequest[] req) {
        for (CreateProductRequest product : req) {
            this.productService.createProduct(product);
        }
        ApiResponse res = new ApiResponse();
        res.setMessage("Products created successfully");
        res.setStatus(true);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody AddProductRequest request) {
        return this.adminProductService.adminAddProduct(request);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return this.adminProductService.adminUpdateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public void updateProduct(@PathVariable Long id) {
        this.adminProductService.adminDeleteProduct(id);
    }

}
