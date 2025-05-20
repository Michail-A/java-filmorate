package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;


    public Film add(Film film) {

        return filmStorage.add(film).orElseThrow();
    }

    public Film update(Film film) {
        return filmStorage.update(film).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + film.getId() + " не существует"));
    }

    public Film get(int id) {
        return filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int id, int userId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return new ArrayList<>(filmStorage.getPopularFilms(count));
    }

    public Mpa getMpa(int id) {
        return mpaStorage.get(id).orElseThrow(() -> new NotFoundException("Рейтинг id=" + id + "не найден"));
    }

    public Genre getGenre(int id) {
        return genreStorage.get(id).orElseThrow(() -> new NotFoundException("Жанр id=" + id + "не найден"));
    }

    public List<Mpa> getMpaAll() {
        return new ArrayList<>(mpaStorage.getAll());
    }

    public List<Genre> getGenreAll() {
        return new ArrayList<>(genreStorage.getAll());
    }

}
