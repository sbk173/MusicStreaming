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

