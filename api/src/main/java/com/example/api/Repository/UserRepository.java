package com.example.api.Repository;

import com.example.api.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User,Long> {

}