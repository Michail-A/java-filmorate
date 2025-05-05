package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private int id = 1;

    private final Map<Integer, User> storage = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя {}", user);
        user.setId(id);
        if (user.getName() == null) {
            log.info("У пользователя не указанно имя, в качестве имени присвоен логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        storage.put(id, user);
        id++;
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя {}", user);
        if (storage.containsKey(user.getId())) {
            log.info("Пользователь id = {} существует", user.getId());
            storage.put(user.getId(), user);
            log.info("Обновлен пользователь {}", user);
            return user;
        } else {
            log.warn("Пользователя с id = {} не существует", user.getId());
            throw new NoSuchElementException("Пользователя с id=" + user.getId() + " не существует");
        }
    }

    @GetMapping
    public List<User> getFilms() {
        log.info("Получен запрос на получение списка всех пользователей");
        return new ArrayList<>(storage.values());
    }
}
