package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("/films")
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private LocalDate minDate = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на добавление фильма");
        film.setId(filmId++);
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Получен запрос на обновление фильма");
        if (!films.containsKey(film.getId())) {
            log.error("Попытка обновить не существующий фильм");
            throw new ValidationException("Такого фильма нет");
        }
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.error("Пустое название фильма");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            log.error("Описание больше 200 символов");
            throw new ValidationException("Описание больше 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(minDate)) {
            log.error("Не корректная дата:" + film.getReleaseDate());
            throw new ValidationException("Дата не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            log.error("Отрицательная продолжительность фильма");
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}
