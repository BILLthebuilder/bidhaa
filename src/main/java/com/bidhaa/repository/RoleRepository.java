package com.bidhaa.repository;

import com.bidhaa.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}
