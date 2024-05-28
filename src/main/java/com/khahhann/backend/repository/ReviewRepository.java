package com.khahhann.backend.repository;

import com.khahhann.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.List;

@RepositoryRestResource(path = "review")
@CrossOrigin("*")
public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.product.id=:productId")
    List<Review> getAllProductReview(@Param("productId")Long productId);
}
