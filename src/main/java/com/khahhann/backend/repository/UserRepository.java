package com.khahhann.backend.repository;

import com.khahhann.backend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path = "user")
@CrossOrigin("*")
public interface UserRepository extends JpaRepository<Users, Long> {
    public Users findByEmail(String email);

    public Users findByEmailAndPassword(String email, String password);

    public boolean existsByEmail(String email);


}
