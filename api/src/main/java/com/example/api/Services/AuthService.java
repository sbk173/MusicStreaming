package com.example.api.Services;
import com.example.api.Services.Handler;

public class AuthService{
    private Handler handler;

    public AuthService(Handler handle){
        this.handler = handle;
    }

    public boolean login(String user_name,String pass){
        boolean present = handler.handle(user_name,pass);
        if (present){
            return true;
        }else {
            throw new RuntimeException("Invalid username/password");
        }

    }
}
