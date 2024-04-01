package com.example.api.Controllers;

import com.example.api.Models.User;
import com.example.api.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")

public class UserController{
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> RegUser(@RequestBody User user){
        try{
            userService.registerUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/profile/{userid}")
    public ResponseEntity<?> getInfo(@PathVariable Long userid){
        try{
            User response = userService.getUser(userid);
            return new ResponseEntity<>(response,HttpStatus.OK); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @GetMapping("login/{username}/{password}")
    public ResponseEntity<?> login(@PathVariable("username") String user_name, @PathVariable("password") String password){
        try{
            User response = userService.login(user_name,password);
            return new ResponseEntity<>(response,HttpStatus.OK); 
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}