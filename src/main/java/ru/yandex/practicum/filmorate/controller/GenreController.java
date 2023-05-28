package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.List;

@RestController
@RequestMapping(path = "/genres")
public class GenreController {
    private final GenreDbStorage genreStorage;

    @Autowired
    public GenreController(GenreDbStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    public List<Genre> getGenres(){
        return genreStorage.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreForId(@PathVariable int id){
        return genreStorage.getGenreForId(id);
    }
}
