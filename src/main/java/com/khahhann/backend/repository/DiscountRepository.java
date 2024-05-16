package com.khahhann.backend.repository;

import com.khahhann.backend.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@RepositoryRestResource(path = "discount")
@CrossOrigin("*")
public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}
