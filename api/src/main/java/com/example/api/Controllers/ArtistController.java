package com.example.api.Controllers;

import com.example.api.Models.Artist;
import com.example.api.Models.Song;
import com.example.api.Services.ArtistService;
import com.example.api.Services.LoggerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/artist")
public class ArtistController{
    final private ArtistService artistService;
    final private LoggerService loggerService = LoggerService.getInstance();

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegArtist(@RequestBody Artist artist){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" POST Request At /api/artist/register");
        try{
            artistService.registerArtist(artist);
            return new ResponseEntity<>(HttpStatus.CREATED); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/profile/{artistid}")
    public ResponseEntity<?> getInfo(@PathVariable Long artistid){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/artist/profile/"+artistid.toString());
        try{
            Artist response = artistService.getArtist(artistid);
            return new ResponseEntity<>(response,HttpStatus.OK); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("login/{username}/{password}")
    public ResponseEntity<?> login(@PathVariable("username") String user_name, @PathVariable("password") String password){
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        loggerService.log(timeStamp+" GET Request At /api/artist/login/{uid}/{password}");
        try{
            Artist response = artistService.login(user_name,password);
            return new ResponseEntity<>(response,HttpStatus.OK); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}