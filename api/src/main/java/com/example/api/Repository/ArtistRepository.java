package com.example.api.Repository;

import com.example.api.Models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist,Long> {
    public Optional<Artist> findByUsername(String username);
}