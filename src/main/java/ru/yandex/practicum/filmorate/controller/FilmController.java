package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;

@RestController("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов");
        return filmStorage.getFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на добавление фильма");
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на обновление фильма");
        filmStorage.updateFilm(film);
        return film;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public String addLike(@PathVariable int id, @PathVariable int userId){
        log.info("Получен запрос на лайк");
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public String deleteLike(@PathVariable int id, @PathVariable int userId){
        log.info("Получен запрос на дизлайк");
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count){
        log.info("Запрос на получение списка популярных фильмов");
        return filmService.getPopularFilm(count);
    }

    @GetMapping("/films/{id}")
    public Film getFilmForId(@PathVariable int id){
        return filmStorage.getFilmForId(id);
    }
}
