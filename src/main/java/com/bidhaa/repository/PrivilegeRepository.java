package com.bidhaa.repository;

import com.bidhaa.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, UUID> {
    Privilege findByName(String name);
}
