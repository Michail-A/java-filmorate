package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public User add(User user) {
        if (user.getName() == null) {
            log.info("У пользователя не указанно имя, в качестве имени присвоен логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        return userStorage.add(user).orElseThrow();
    }

    public User update(User user) {
        return userStorage.update(user).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + user.getId() + " не существует"));
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(int id) {
        return userStorage.get(id).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + id + " не существует"));
    }

    public void addFriend(int userId, int friendId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        userStorage.get(friendId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + friendId + " не существует"));
        userStorage.addFriend(userId, friendId);
        log.info("Пользователи id={} и id={} добавлены в друзья", userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        userStorage.get(friendId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + friendId + " не существует"));
        userStorage.deleteFriend(userId, friendId);
        log.info("Пользователи id={} и id={} удалены из друзей", userId, friendId);
    }

    public List<User> getFriends(int userId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));

        userStorage.get(otherId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + otherId + " не существует"));

        List<User> userFriends = userStorage.getFriends(userId);
        List<User> otherFriends = userStorage.getFriends(otherId);

        return userFriends.stream()
                .filter(otherFriends::contains)
                .collect(Collectors.toList());
    }

}
