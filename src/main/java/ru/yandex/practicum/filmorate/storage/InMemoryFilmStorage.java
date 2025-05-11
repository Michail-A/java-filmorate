package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> storage = new HashMap<>();

    private int id = 1;

    @Override
    public Film add(Film film) {
        film.setId(id);
        storage.put(id, film);
        id++;
        log.info("Добавлен фильм:{}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!storage.containsKey(film.getId())) {
            log.warn("Фильма с id={} не существует", film.getId());
            throw new NotFoundException("Фильма с id=" + film.getId() + " не существует");
        }
        storage.put(film.getId(), film);
        log.info("Обновлен фильм:{}", film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Film get(int id) {
        if (!storage.containsKey(id)) {
            log.warn("Фильма с id={} не существует", id);
            throw new NotFoundException(String.format("Фильма id=%s не существует", id));
        }
        return storage.get(id);
    }
}
