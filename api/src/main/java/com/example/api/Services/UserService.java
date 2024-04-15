package com.example.api.Services;

// import com.example.api.Dto.SongDto;
import com.example.api.Models.User;
import com.example.api.Services.Handler;
import com.example.api.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

public class AuthService{
    private Handler handler;

    public AuthService(Handler handle){
        this.handler = handle;
    }

    public User login(String user_name,String pass){
        Optional<User> temp = handler.handle(user_name,pass);
        if (temp.isPresent()){
            return temp.get();
        }else {
            throw new RuntimeException("Invalid username/password");
        }
        
    }
}

@Service
public class UserService {
    final private UserRepository userRepo;

    @Autowired
    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User registerUser(User user){
        // Fill here
        String user_name = user.getUsername();
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

    // public User login(String user_name, String pass){
    //     // Fill here
    //     try{
    //         Optional<User> response = userRepo.findByUsername(user_name);
    //         if (response.isPresent()){
    //             User u = response.get();
    //             if (u.getPassword().equals(pass)){
    //                 return u;
    //             }
    //         }
    //         throw new RuntimeException("Incorrect username/password");
            
    //     }
    //     catch(Exception e) {
    //         throw new RuntimeException("Error logging in");
    //     }

    // }

    public User login(String user_name, String pass){
        // Fill here
        try{
            Handler handler = new userExistsCheckHandler(userRepo)
                                .setNextHandler(new validPasswordHandler(userRepo));
            
            AuthService serv = new AuthService(handler);
            Optional<User> temp = serv.login(user_name,pass);
            if (temp.isPresent()){
                return temp.get();
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