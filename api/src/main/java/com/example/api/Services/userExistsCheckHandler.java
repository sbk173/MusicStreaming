package com.example.api.Services;

// import com.example.api.Dto.SongDto;
import com.example.api.Models.User;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.api.Services.Handler;

import java.util.Optional;

public class userExistsCheckHandler extends Handler{
    private UserRepository userRepo;

    public  userExistsCheckHandler(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public boolean handle(String username, String password) {
        Optional<User> temp = userRepo.findByUsername(username);
        if (!temp.isPresent()){
            throw new RuntimeException("Incorrect username/password");
        }
        else{
            return handleNext(username,password);
        }
    }
}
