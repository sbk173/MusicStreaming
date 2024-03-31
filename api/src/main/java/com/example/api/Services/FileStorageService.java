package com.example.api.Services;
import com.example.api.Models.Song;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String upload(MultipartFile file);

    Resource download(String fileName);
}