package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserStorage userStorage;
    private final UserService userService;
    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }


    @GetMapping
    public List<User> getUsers() {
        log.info("Запрос на получение пользователей");
        return userStorage.getUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Запрос на добавление пользователя");
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Запрос на обновление пользователя");
        return userStorage.updateUser(user);
    }

  @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Запрос на добавление в друзья");
        userStorage.addFriend(id, friendId);
        return userStorage.getUserForId(id);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Запрос на удаление друзей");
        userStorage.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Запрос на получение списка друзей пользователя");
        return userStorage.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Запрос на получение списка общих друзей");
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserForId(@PathVariable int id) {
        return userStorage.getUserForId(id);
    }
}
