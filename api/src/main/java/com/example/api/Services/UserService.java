package com.example.api.Services;

// import com.example.api.Dto.SongDto;
import com.example.api.Models.User;
import com.example.api.Repository.UserRepository;
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
public class UserService {
    final private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User registerUser(User user){
        // Fill here
        String user_name = user.username;
        Optional<User> temp = userRepo.findByUsername(user_name);
        if (!temp.isPresent()){
            try{
                User response = userRepo.save(user);
                return response;
            }
            catch(Exception e) {
                throw new RuntimeException("Registration failed");
            }
        }

        throw new RuntimeException("Username already exists");

    }

    public User login(String user_name, String pass){
        // Fill here
        try{
            Optional<User> response = userRepo.findByUsername();
            if (response.isPresent()){
                User u = response.get();
                if (u.password.equals(pass)){
                    return u;
                }
            }
            throw new RuntimeException("Incorrect username/password");
            
        }
        catch(Exception e) {
            throw new RuntimeException("Error logging in");
        }

    }

    public User getUser(Long id){
        try{
            Optional<User> response = userRepo.findById(id);

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