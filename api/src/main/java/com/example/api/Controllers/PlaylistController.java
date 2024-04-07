package com.example.api.Controllers;

import com.example.api.Models.Playlist;
import com.example.api.Models.Song;
import com.example.api.Services.LoggerService;
import com.example.api.Services.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/playlist")
public class PlaylistController {

    private PlaylistService playlistService;
    final private LoggerService loggerService = LoggerService.getInstance();

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping("/addPlaylist")
    public ResponseEntity<Playlist> createPlaylist(@RequestBody Playlist playlist) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" POST Request At /api/playlist/addPlaylist");
        Playlist savedPlaylist = playlistService.createPlaylist(playlist);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaylist);
    }

    @GetMapping("/getAllPlaylists")
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/playlist/getAllPlaylist");
        List<Playlist> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/getPlaylistById/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable Long id) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/playlist/getPlaylistById/"+id.toString());
        Optional<Playlist> playlist = playlistService.getPlaylistById(id);
        return playlist.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{playlistId}/addUser/{userId}")
    public ResponseEntity<String> addUserToPlaylist(@PathVariable Long playlistId, @PathVariable Long userId) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" POST Request At /api/playlist/"+playlistId.toString()+"/addUSer/"+
            userId.toString());
        boolean success = playlistService.addUserToPlaylist(playlistId, userId);
        if (success) {
            return ResponseEntity.ok("User added to playlist successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{playlistId}/addSong/{songId}")
    public ResponseEntity<String> addSongToPlaylist(@PathVariable Long playlistId, @PathVariable Long songId) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" POST Request At /api/playlist/"+ playlistId.toString()+"/addSong/"+ songId.toString());
        boolean success = playlistService.addSongToPlaylist(playlistId, songId);
        if (success) {
            return ResponseEntity.ok("Song added to playlist successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getPlaylists/{userId}")
    public ResponseEntity<?> getAllPlaylistsByUser(@PathVariable Long userId){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/playlist/getPlaylist/"+userId.toString());
        return new ResponseEntity<List<Playlist>>(playlistService.getAllPlaylistsByUserId(userId),HttpStatus.OK);
    }

    @GetMapping("/getSongs/{playlistId}")
    public ResponseEntity<?> getAllSongsInPlaylist(@PathVariable Long playlistId){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/playlist/getSongs/"+ playlistId.toString());
        return new ResponseEntity<Set<Song>>(playlistService.getAllSongsInPlaylist(playlistId),HttpStatus.OK);
    }
}
