package com.khahhann.backend.repository;

import com.khahhann.backend.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RolesRepository extends JpaRepository<Roles, UUID> {
    public List<Roles> findByRoleName(String roleName);
}
