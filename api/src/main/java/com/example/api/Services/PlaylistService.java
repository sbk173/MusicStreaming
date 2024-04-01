package com.example.api.Services;

import com.example.api.Repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PlaylistService {
    private PlaylistRepository playlistRepository;
    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }


}
