package com.example.api.Repository;

import com.example.api.Models.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Playlist,Long> {

}