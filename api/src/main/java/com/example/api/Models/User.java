package com.example.api.Models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.api.Models.Song;
import com.example.api.Models.Playlist;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
//    private List<Song> preference;
//    private List<Song> history;


    @ManyToMany(mappedBy = "users")
    private Set<Playlist> playlists = new HashSet<>();
}