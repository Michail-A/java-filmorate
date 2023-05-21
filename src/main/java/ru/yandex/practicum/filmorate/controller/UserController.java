package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@RestController("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Запрос на получение пользователей");
        return userStorage.getUsers();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) throws ValidationException {
        log.info("Запрос на добавление пользователя");
        return userStorage.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        log.info("Запрос на обновление пользователя");
        return userStorage.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public String addFriend(@PathVariable int id, @PathVariable int friendId){
        log.info("Запрос на добавление в друзья");
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable int id, @PathVariable int friendId){
        log.info("Запрос на удаление друзей");
        return userService.deleteFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Set<Integer> getFriends(@PathVariable int id){
        log.info("Запрос на получение списка друзей пользователя");
        return userStorage.getUserForId(id).getFriends();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<Integer> getCommonFriends(@PathVariable int id, @PathVariable  int otherId){
        log.info("Запрос на получение списка общих друзей");
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/users/{id}")
    public User getUserForId(@PathVariable int id){
        return userStorage.getUserForId(id);
    }
}
