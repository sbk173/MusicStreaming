package com.example.api.Services;

import com.example.api.Exceptions.FileStorageException;
import com.example.api.Models.Song;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {
    private final String uploadDir = "./uploads"; // Directory where files will be stored

    public LocalFileStorageService() {
        // Create the upload directory if it doesn't exist
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Override
    public String upload(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Generate a unique filename
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);
            return uniqueFileName;
        } catch (IOException ex) {
            throw new FileStorageException("Failed to store file " + fileName, ex);
        }

    }

    @Override
    public Resource download(String fileName) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            InputStream inputStream = Files.newInputStream(filePath);
            return new InputStreamResource(inputStream);
        } catch (IOException ex) {
            throw new FileStorageException("Failed to download file " + fileName, ex);
        }
    }

    public void delete(String filename){
        Path filePath = Paths.get(uploadDir, filename);
        try {
            // Attempt to delete the file
            Files.delete(filePath);

        } catch (IOException e) {
            // Handle the exception if the file deletion fails
            e.printStackTrace();
        }
    }
}