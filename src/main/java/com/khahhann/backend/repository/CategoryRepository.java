package com.khahhann.backend.repository;

import com.khahhann.backend.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "category")
@CrossOrigin("*")
public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Category findByName(String name);
    @Query("Select c from Category c WHERE c.name = :name AND c.parentCategory.name = :parentCategoryName")
    public Category findByNameAndParent(@Param("name") String name,
                                        @Param("parentCategoryName") String parentCategoryName);
}
