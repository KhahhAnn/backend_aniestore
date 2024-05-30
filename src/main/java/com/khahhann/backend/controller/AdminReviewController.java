package com.khahhann.backend.controller;


import com.khahhann.backend.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin/review")
public class AdminReviewController {
    private ReviewService reviewService;
    @DeleteMapping("/{id}")
    public void deleteReviewHandle(@PathVariable Long id) {
        this.reviewService.deleteReview(id);
    }
}
