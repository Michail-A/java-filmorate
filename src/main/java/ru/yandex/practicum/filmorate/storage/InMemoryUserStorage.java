package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> storage = new HashMap<>();

    private int id = 1;

    @Override
    public User add(User user) {
        user.setId(id);
        storage.put(id, user);
        id++;
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!storage.containsKey(user.getId())) {
            log.warn("Пользователя с id = {} не существует", user.getId());
            throw new NotFoundException("Пользователя с id=" + user.getId() + " не существует");
        }
        storage.put(user.getId(), user);
        log.info("Обновлен пользователь {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public User get(int id) {
        if (!storage.containsKey(id)) {
            throw new NotFoundException(String.format("Пользователя id=%s не существует", id));
        }
        return storage.get(id);
    }
}
