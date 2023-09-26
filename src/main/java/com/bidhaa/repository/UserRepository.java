package com.bidhaa.repository;

import com.bidhaa.model.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@CacheConfig(cacheNames = "users")
public interface UserRepository extends JpaRepository<User,UUID> {
    @Cacheable
    User findByEmail(String email);

    List<User> findAllByStatus(boolean status);
}

