package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;
    private LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(filmId++);
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new RuntimeException("Такого фильма нет");
        }
        validate(film);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmForId(int id) {
        if(!films.containsKey(id)){
            throw new RuntimeException();
        }
        return films.get(id);
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            throw new ValidationException("Описание больше 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(minDate)) {
            throw new ValidationException("Дата не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}
