package com.example.api.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String artist;
    private String genre;
    private String duration;
    private String filename;
    private Long artistid;

    @ManyToMany(mappedBy = "songs")
    private Set<Playlist> playlists = new HashSet<>();

}


