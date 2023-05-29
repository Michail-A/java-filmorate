package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmForId(int id);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Set<Film> getLikes(int count);
}
