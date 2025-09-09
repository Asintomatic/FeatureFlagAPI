package com.bytescolab.featureflag.repository;

import com.bytescolab.featureflag.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername (String name);
    boolean existsByUsername(String username);
}
