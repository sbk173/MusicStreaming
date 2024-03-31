package com.example.api.Services;

import com.example.api.Dto.SongDto;
import com.example.api.Models.Song;
import com.example.api.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SongService {
    final private SongRepository songRepo;
    final private LocalFileStorageService storage;

    @Autowired
    public SongService(SongRepository songRepo, LocalFileStorageService storage) {
        this.songRepo = songRepo;
        this.storage = storage;
    }

    public void uploadSong(MultipartFile file, SongDto song){
        String name = storage.upload(file);
        song.setFilename(name);
        Song temp = processDto(song);
        songRepo.save(temp);
    }

    public SongDto getSongDetails(Long Id){
        Optional<Song> song = songRepo.findById(Id);
        if(song.isPresent()){
            return toDto(song.get());
        }
        throw new RuntimeException("Song Not found");
    }

    public void deleteSong(Long Id){
        Optional<Song> song = songRepo.findById(Id);
        if(song.isPresent()){
            storage.delete(song.get().getFilename());
            songRepo.deleteById(Id);
        }
    }

    public Song processDto(SongDto songDto){
        Song song = new Song();
        song.setTitle(songDto.getTitle());
        song.setDuration(songDto.getDuration());
        song.setArtist(songDto.getArtist());
        song.setGenre(songDto.getGenre());
        song.setFilename(songDto.getFilename());
        return song;
    }

    public SongDto toDto(Song song){
        SongDto songDto = new SongDto();
        songDto.setTitle(song.getTitle());
        songDto.setArtist(song.getArtist());
        songDto.setId(song.getId());
        songDto.setDuration(song.getDuration());
        songDto.setFilename(song.getFilename());
        songDto.setGenre(song.getGenre());
        return songDto;
    }


    public SongDto getSongById(Long id) {
        Optional<Song> song = songRepo.findById(id);
        if(song.isPresent()) return toDto(song.get());
        else throw new RuntimeException("song not found");
    }

    public Resource downloadSong(String filename) {
        Resource song = storage.download(filename);
        return song;
    }

    public List<SongDto> getAllSongs() {
        List<Song> songs= songRepo.findAll();
        return songs.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<SongDto> searchSongs(String context){
        List<SongDto> songs = getAllSongs();
        List<SongDto> response = songs.stream().filter(dto -> containsContext(dto,context)).collect(Collectors.toList());
        return response;
    }

    public boolean containsContext(SongDto song, String context){
        return song.getTitle().toLowerCase().contains(context.toLowerCase()) ||
                song.getArtist().toLowerCase().contains(context.toLowerCase()) ||
                song.getGenre().toLowerCase().contains(context.toLowerCase());
    }

}
