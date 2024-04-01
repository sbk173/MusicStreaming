package com.example.api.Repository;

import com.example.api.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String username);
}