package com.example.api.Services;

// import com.example.api.Dto.SongDto;
import com.example.api.Models.User;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public abstract class Handler{
    private Handler next;

    public Handler setNextHandler(Handler next) {
        this.next = next;
        return next;
    }

    public abstract boolean handle(String userName, String password);

    protected boolean handleNext(String userName, String password){
        if (next == null){
            return true;
        }
        return next.handle(userName,password);
    }

}

public class userExistsCheckHandler extends Handler{
    private UserRepository userRepo;

    public  userExistsCheckHandler(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public boolean handle(String username, String password) {
        Optional<User> temp = userRepo.findByUsername(user_name);
        if (!temp.isPresent()){
            throw new RuntimeException("Incorrect username/password");
        }
        else{
            return handleNext(username,password);
        }
    }
}

public class validPasswordHandler extends Handler{
    private UserRepository userRepo;

    public  userExistsCheckHandler(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public boolean handle(String username, String password) {
        Optional<User> temp = userRepo.findByUsername(user_name);
        if (!temp.isPresent()){
            throw new RuntimeException("Incorrect username/password");
        }
        else{
            User u = temp.get();
            if (u.getPassword().equals(password)){
                if (handleNext(username,password))
                return u;
            }
            throw new RuntimeException("Incorrect username/password");
        }
    }
}  