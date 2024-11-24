package com.khahhann.backend.service.impl;

import com.khahhann.backend.exception.ProductException;
import com.khahhann.backend.model.Category;
import com.khahhann.backend.model.Product;
import com.khahhann.backend.repository.CategoryRepository;
import com.khahhann.backend.repository.ProductRepository;
import com.khahhann.backend.request.CreateProductRequest;
import com.khahhann.backend.service.ProductService;
import com.khahhann.backend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Override
    public Product createProduct(CreateProductRequest productRequest) {
        Category topLevel = this.categoryRepository.findByName(productRequest.getTopLevelCategory());
        if(topLevel == null) {
            Category topLevelCategory = new Category();
            topLevelCategory.setName(productRequest.getTopLevelCategory());
            topLevelCategory.setLevel(1);
            topLevel = this.categoryRepository.saveAndFlush(topLevelCategory);
        }
        Category secondLevel = this.categoryRepository.findByNameAndParent(productRequest.getSecondLevelCategory(), topLevel.getName());
        if(secondLevel == null) {
            Category secondLevelCategory = new Category();
            secondLevelCategory.setName(productRequest.getSecondLevelCategory());
            secondLevelCategory.setParentCategory(topLevel);
            secondLevelCategory.setLevel(2);
            secondLevel = this.categoryRepository.saveAndFlush(secondLevelCategory);
        }
        Category thirdLevel = this.categoryRepository.findByNameAndParent(productRequest.getThirdLevelCategory(), secondLevel.getName());
        if(thirdLevel == null) {
            Category thirdLevelCategory = new Category();
            thirdLevelCategory.setName(productRequest.getThirdLevelCategory());
            thirdLevelCategory.setParentCategory(secondLevel);
            thirdLevelCategory.setLevel(3);
            thirdLevel = this.categoryRepository.saveAndFlush(thirdLevelCategory);
        }

        Product product = new Product();
        product.setTitle(productRequest.getTitle());
        product.setColor(productRequest.getColor());
        product.setBrand(productRequest.getBrand());
        product.setDescription(productRequest.getDescription());
        product.setDiscountedPrice(productRequest.getDiscountedPrice());
        product.setDiscountPercent(productRequest.getDiscountPercent());
        product.setPrice(productRequest.getPrice());
        product.setSizes(productRequest.getSize());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(thirdLevel);
        product.setCreatedAt(LocalDate.now());
        product.setImageUrl(productRequest.getImageUrl());

        Product savedProduct = this.productRepository.saveAndFlush(product);

        return savedProduct;
    }

    @Override
    public String deleteProduct(Long productId) throws ProductException {
        Product product = this.findProductById(productId);
        product.getSizes().clear();
        this.productRepository.delete(product);
        return "Product delete successfully";
    }

    @Override
    public Product updateProduct(Long productId, Product req) throws ProductException {
        Product product = this.findProductById(productId);
        if(req.getQuantity() != 0) {
            product.setQuantity(req.getQuantity());
        }
        return this.productRepository.saveAndFlush(product);
    }

    @Override
    public Product findProductById(Long id) throws ProductException {
        Optional<Product> opt = this.productRepository.findById(id);
        if(opt.isPresent()) {
            return opt.get();
        }
        throw new ProductException("Product not found with id - " + id);
    }

    @Override
    public List<Product> findProductByCategory(String category) {
        return null;
    }

    @Override
    public Page<Product> getAllProduct(String category, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount, String sort, String stock, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        List<Product> products = new ArrayList<>();

        if ("all".equalsIgnoreCase(category)) {
            products = this.productRepository.findAll();
        } else {
            products = this.productRepository.filterProducts(category, minPrice, maxPrice, minDiscount, sort);
        }
        if(!colors.isEmpty()) {
            products = products.stream().filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
                    .collect(Collectors.toList());
        }
        if(stock != null) {
            if(stock.equals("in_stock")) {
                products = products.stream().filter(p -> p.getQuantity() > 0).collect(Collectors.toList());
            }
            else if(stock.equals("out_of_stock")) {
                products = products.stream().filter(p -> p.getQuantity() < 1).collect(Collectors.toList());
            }
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
        List<Product> pageContent  = products.subList(startIndex, endIndex);
        Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
        return filteredProducts;
    }

    @Override
    public List<Product> findAllProducts() {
        return this.productRepository.findAll();
    }
}
