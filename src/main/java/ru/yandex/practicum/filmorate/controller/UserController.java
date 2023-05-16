package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос на получение пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        log.info("Запрос на добавление пользователя");
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Поле 'name' заполнено из логина");
            user.setName(user.getLogin());
        }
        validate(user);
        user.setId(userId++);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("Запрос на обновление пользователя");
        if (!users.containsKey(user.getId())) {
            log.error("Попытка обновить несуществующего пользователя");
            throw new ValidationException("Такого пользователя нет");
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    private void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Не верный эмэйл");
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Пустой логин");
            throw new ValidationException("Некорректный логин");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Не верная дата рождения");
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
