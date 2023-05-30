package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@RestController
@RequestMapping(path = "/genres")
public class GenreController {

    private static final Logger log = LoggerFactory.getLogger(GenreController.class);
    private final GenreStorage genreStorage;

    @Autowired
    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен запрос на получение списка жанров");
        return genreStorage.getGenres();
    }

    @GetMapping("/{id}")
    public Genre getGenreForId(@PathVariable int id) {
        log.info("Получен запрос на получение жанра id= " + id);
        return genreStorage.getGenreForId(id);
    }
}
