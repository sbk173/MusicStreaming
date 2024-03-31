package com.example.api.Services;

// import com.example.api.Dto.SongDto;
import com.example.api.Models.Artist;
import com.example.api.Repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArtistService {
    final private ArtistRepository artistRepo;

    @Autowired
    public ArtistService(ArtistRepository artistRepo) {
        this.artistRepo = artistRepo;
    }

    public Artist registerArtist(Artist artist){
        // Fill here
        String user_name = artist.username;
        Optional<Artist> temp = artistRepo.findByUsername(user_name);
        if (!temp.isPresent()){
            try{
                Artist response = artistRepo.save(artist);
                return response;
            }
            catch(Exception e) {
                throw new RuntimeException("Registration failed");
            }
        }

        throw new RuntimeException("Username already exists");

    }

    public Artist login(String user_name, String pass){
        // Fill here
        try{
            Optional<Artist> response = artistRepo.findByUsername();
            if (response.isPresent()){
                Artist a = response.get();
                if (a.password.equals(pass)){
                    return a;
                }
            }
            throw new RuntimeException("Incorrect username/password");
            
        }
        catch(Exception e) {
            throw new RuntimeException("Error logging in");
        }

    }

    public Artist getArtist(Long id){
        try{
            Optional<Artist> response = artistRepo.findById(id);

            if (response.isPresent()){

                return response.get();
            }
            throw new RuntimeException("Login");
        }
        catch (Exception e){
            throw new RuntimeException("Error");
        }
        
    }

}