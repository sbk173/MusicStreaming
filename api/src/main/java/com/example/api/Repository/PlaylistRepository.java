package com.example.api.Repository;

import com.example.api.Models.Playlist;
import com.example.api.Models.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist,Long> {

    @Query("SELECT DISTINCT p FROM Playlist p JOIN p.users u WHERE u.id = :userId")
    List<Playlist> findPlaylistsByUserId(@Param("userId") Long userId);

    @Query("SELECT p.songs FROM Playlist p WHERE p.id = :playlistId")
    Optional<List<Song>> findSongsByPlaylistId(@Param("playlistId") Long playlistId);
}
