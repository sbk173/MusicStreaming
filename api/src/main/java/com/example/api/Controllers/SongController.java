package com.example.api.Controllers;

import com.example.api.Dto.SongDto;
import com.example.api.Models.Song;
import com.example.api.Services.LoggerService;
import com.example.api.Services.SongService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/song")
public class SongController {
    final private SongService songService;
    final private LoggerService loggerService = LoggerService.getInstance();

    @Autowired
    public SongController(SongService songService) {
        this.songService = songService;
    }

    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> addSong( @RequestParam("data") String songData, @RequestParam("file") MultipartFile file){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" POST Request At /api/song/upload");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            SongDto song = objectMapper.readValue(songData, SongDto.class);
            songService.uploadSong(file,song);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/getSong/{id}")
    public ResponseEntity<?> getSong(@PathVariable Long id){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/song/getSong/"+id.toString());
        SongDto response = songService.getSongById(id);
        return new ResponseEntity<SongDto>(response,HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSongs(){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/song/getAll");
        List<SongDto> response = songService.getAllSongs();
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<List<SongDto>>(response,HttpStatus.OK);
        }

    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<?> downloadSong(@PathVariable String filename){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/song/download/"+filename);
        Resource obj = songService.downloadSong(filename);
        return new ResponseEntity<Resource>(obj,HttpStatus.OK);
    }

    @GetMapping("/search/{context}")
    public ResponseEntity<?> searchSong(@PathVariable String context){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/song/"+context);
        List<SongDto> response = songService.searchSongs(context);
        if(response.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<List<SongDto>>(response,HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSong(@PathVariable Long id){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" DELETE Request At /api/song/delete/"+id.toString());
        songService.deleteSong(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getSongByArtist/{id}")
    public ResponseEntity<?> getSonByArtist(@PathVariable Long id){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/song/getSongByArtist/"+id.toString());
        List<Song> songs = songService.getAllSongsByArtist(id);
        return new ResponseEntity<>(songs,HttpStatus.OK);
    }

}
