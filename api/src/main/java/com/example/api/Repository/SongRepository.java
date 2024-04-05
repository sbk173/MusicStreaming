package com.example.api.Repository;

import com.example.api.Models.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song,Long> {
    public Optional<List<Song>> findAllByArtistid(Long id);
}
