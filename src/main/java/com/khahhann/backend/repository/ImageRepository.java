package com.khahhann.backend.repository;

import com.khahhann.backend.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.UUID;

@RepositoryRestResource(path = "img")
@CrossOrigin("*")
public interface ImageRepository extends JpaRepository<Images, UUID> {
}
