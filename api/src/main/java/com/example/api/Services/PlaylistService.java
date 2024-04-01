package com.example.api.Services;

import com.example.api.Models.Playlist;
import com.example.api.Models.Song;
import com.example.api.Models.User;
import com.example.api.Repository.PlaylistRepository;
import com.example.api.Repository.SongRepository;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PlaylistService {
    private PlaylistRepository playlistRepository;
    private UserRepository userRepository;

    private SongRepository songRepository;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository, UserRepository userRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    public Playlist createPlaylist(Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public List<Playlist> getAllPlaylists() {
        return playlistRepository.findAll();
    }

    public Optional<Playlist> getPlaylistById(Long id) {
        return playlistRepository.findById(id);
    }

    public boolean addUserToPlaylist(Long playlistId, Long userId) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalPlaylist.isPresent() && optionalUser.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            User user = optionalUser.get();
            playlist.getUsers().add(user);
            playlistRepository.save(playlist);
            return true;
        } else {
            return false;
        }
    }

    public boolean addSongToPlaylist(Long playlistId, Long songId) {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        Optional<Song> optionalSong = songRepository.findById(songId);
        if (optionalPlaylist.isPresent() && optionalSong.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            Song song = optionalSong.get();
            playlist.getSongs().add(song);
            playlistRepository.save(playlist);
            return true;
        } else {
            return false;
        }
    }

    public List<Playlist> getAllPlaylistsByUserId(Long userId) {
        return playlistRepository.findByUsersId(userId);
    }

    public Set<Song> getAllSongsInPlaylist(Long playlistId) {
        Optional<Playlist> playlist = playlistRepository.findById(playlistId);
        if(playlist.isPresent()){
            Set<Song> songs = playlist.get().getSongs();
            return songs;
        }
        else throw new RuntimeException("Playlist Not found");
    }



}
