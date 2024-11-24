package com.khahhann.backend.controller;

import com.khahhann.backend.model.Discount;
import com.khahhann.backend.response.ApiResponse;
import com.khahhann.backend.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/discount")
public class AdminDiscountController {
    private DiscountService discountService;
    @PostMapping()
    public ApiResponse addDiscount(@RequestBody Discount discount) {
        return this.discountService.addDiscount(discount);
    }
    @PutMapping()
    public Discount updateDiscount(@RequestBody Discount discount) {
        return this.discountService.updateCategory(discount);
    }
    @DeleteMapping("/{id}")
    public void deleteDiscount(@PathVariable("id") UUID id) {
        this.discountService.deleteDiscount(id);
    }


    @GetMapping("/{discountName}")
    public Discount getDiscountByDiscountName(@PathVariable("discountName") String discountName) {
        return this.discountService.getDiscountByName(discountName);
    }
}
