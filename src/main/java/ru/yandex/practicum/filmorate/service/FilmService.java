package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmForId(filmId);
        film.addLike(userId);
        filmStorage.updateFilm(film);
        log.info("Пользователь " + userId + " поставил лайк фильму " + filmId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmForId(filmId);
        film.deleteLike(userId);
        filmStorage.updateFilm(film);
        log.info("Пользователь " + userId + " удалил лайк фильму " + filmId);
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.getFilms().stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
