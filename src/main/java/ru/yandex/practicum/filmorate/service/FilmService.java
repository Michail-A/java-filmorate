package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public String addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmForId(filmId);
        film.addLike(userId);
        filmStorage.updateFilm(film);
        return "Пользователь " + userId + " поставил лайк фильму " + filmId;
    }

    public String deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilmForId(filmId);
        film.deleteLike(userId);
        filmStorage.updateFilm(film);
        return "Пользователь " + userId + " удалил лайк фильму " + filmId;
    }

    public List<Film> getPopularFilm(int count) {
        List<Film> films = filmStorage.getFilms();
        Comparator<Film> likeComparator = new Comparator<Film>() {
            @Override
            public int compare(Film o1, Film o2) {
                return o1.getLikes().size() - o2.getLikes().size();
            }
        };
        films.sort(likeComparator);
        Collections.reverse(films);
        if(films.size()<count){
            count= films.size();
        }
        List<Film> popularFilms = films.subList(0, count);
        return popularFilms;
    }
}
