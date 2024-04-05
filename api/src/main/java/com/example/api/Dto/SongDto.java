package com.example.api.Dto;

import com.example.api.Models.Artist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SongDto {
    private Long id;
    private String title;
    private String artist;
    private String genre;
    private String duration;
    private String filename;
    private Long artistid;
}

