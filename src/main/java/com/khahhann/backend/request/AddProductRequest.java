package com.khahhann.backend.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AddProductRequest {
    private String title;
    private String description;
    private double price;
    private double discountedPrice;
    private int quantity;
    private String brand;
    private String color;
    private String imageUrl;
    private Long categoryId;
}
