package com.khahhann.backend.controller;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {
    private ProductService productService;
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findProductByCategoryHandle(@RequestParam String category,
                                                                     @RequestParam List<String> color,
                                                                     @RequestParam List<String> size,
                                                                     @RequestParam Integer minPrice,
                                                                     @RequestParam Integer maxPrice,
                                                                     @RequestParam Integer minDiscount,
                                                                     @RequestParam String sort,
                                                                     @RequestParam String stock,
                                                                     @RequestParam Integer pageNumber,
                                                                     @RequestParam Integer pageSize) {
        Page<Product> res = this.productService.getAllProduct(category, color, size, minPrice, maxPrice, minDiscount, sort, stock, pageNumber, pageSize);
        return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
    }

    @GetMapping("/products/id/{productId}")
    public ResponseEntity<Product> findProductByIdHandle(@PathVariable Long productId) throws ProductException {
        Product product = this.productService.findProductById(productId);
        return new ResponseEntity<>(product, HttpStatus.ACCEPTED);
    }

//    @GetMapping("/products/search")
//    public ResponseEntity<List<Product>> searchProductHandle(@RequestParam String req)  {
//        List<Product> productList = this.productService.searchProduct(req);
//        return new ResponseEntity<>(productList, HttpStatus.OK);
//    }
}
