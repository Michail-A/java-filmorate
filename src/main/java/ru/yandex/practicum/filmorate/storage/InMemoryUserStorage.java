package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> storage = new HashMap<>();

    private int id = 1;

    @Override
    public Optional<User> add(User user) {
        user.setId(id);
        storage.put(id, user);
        id++;
        log.info("Добавлен пользователь {}", user);
        return Optional.ofNullable(storage.get(user.getId()));
    }

    @Override
    public Optional<User> update(User user) {
        if (!storage.containsKey(user.getId())) {
            log.warn("Пользователя с id = {} не существует", user.getId());
            return Optional.empty();
        }
        storage.put(user.getId(), user);
        log.info("Обновлен пользователь {}", user);
        return Optional.of(storage.get(user.getId()));
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public Optional<User> get(int id) {
        return Optional.ofNullable(storage.get(id));
    }
}
