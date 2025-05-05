package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private int id = 1;

    private final Map<Integer, Film> storage = new HashMap<>();


    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        film.setId(id);
        storage.put(id, film);
        id++;
        log.info("Добавлен фильм:" + film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запроc на обновление фильма id={}", film.getId());
        if (storage.containsKey(film.getId())) {
            log.info("Фильм с id={} существует", film.getId());
            storage.put(film.getId(), film);
            log.info("Обновлен фильм:{}", film);
            return film;
        } else {
            log.warn("Фильма с id={} не существует", film.getId());
            throw new NoSuchElementException("Фильма с id=" + film.getId() + " не существует");
        }
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка всех фильмов");
        return new ArrayList<>(storage.values());
    }

}
