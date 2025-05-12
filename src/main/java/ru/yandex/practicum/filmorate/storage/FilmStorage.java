package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Optional<Film> add(Film film);

    Optional<Film> update(Film film);

    List<Film> getAll();

    Optional<Film> get(int id);
}
