package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> storage = new HashMap<>();

    private int id = 1;

    @Override
    public Optional<Film> add(Film film) {
        film.setId(id);
        storage.put(id, film);
        id++;
        log.info("Добавлен фильм:{}", film);
        return Optional.of(storage.get(film.getId()));
    }

    @Override
    public Optional<Film> update(Film film) {
        if (!storage.containsKey(film.getId())) {
            log.warn("Фильма с id={} не существует", film.getId());
            return Optional.empty();
        }
        storage.put(film.getId(), film);
        log.info("Обновлен фильм:{}", film);
        return Optional.of(storage.get(film.getId()));
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<Film> get(int id) {
        return Optional.ofNullable(storage.get(id));
    }
}
