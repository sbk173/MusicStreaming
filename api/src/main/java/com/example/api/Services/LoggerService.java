package com.example.api.Services;

import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

@Service
public class LoggerService {
    private static LoggerService instance;
    private PrintWriter writer;

    private LoggerService() {
        // Private constructor to prevent instantiation
        try {
            // Open the file in append mode
            writer = new PrintWriter(new FileWriter("log.txt", true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized LoggerService getInstance() {
        if (instance == null) {
            instance = new LoggerService();
        }
        return instance;
    }

    public void log(String message) {
        // Logic to log messages
        writer.println(message);
        writer.flush();
    }
}