package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
public class GenreController {

    private final FilmService filmService;

    @GetMapping
    public List<Genre> getGenres() {
        log.info("Получен запрос на получение списка жанров");
        return filmService.getGenreAll();
    }

    @GetMapping("/{id}")
    public Genre getGenreForId(@PathVariable int id) {
        log.info("Получен запрос на получение жанра id= {}", id);
        return filmService.getGenre(id);
    }
}
