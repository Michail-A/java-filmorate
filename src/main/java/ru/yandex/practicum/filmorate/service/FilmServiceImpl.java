package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film add(Film film) {
        return filmStorage.add(film).orElseThrow();
    }

    @Override
    public Film update(Film film) {
        return filmStorage.update(film).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + film.getId() + " не существует"));
    }

    @Override
    public Film get(int id) {
        return filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
    }

    @Override
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    @Override
    public void addLike(int id, int userId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        Film film = filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
        film.addLike(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        Film film = filmStorage.get(id).orElseThrow(()
                -> new NotFoundException("Фильма с id=" + id + " не существует"));
        film.deleteLike(userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
