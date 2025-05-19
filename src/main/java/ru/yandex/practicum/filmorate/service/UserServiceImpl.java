package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User add(User user) {
        if (user.getName() == null) {
            log.info("У пользователя не указанно имя, в качестве имени присвоен логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        return userStorage.add(user).orElseThrow();
    }

    @Override
    public User update(User user) {
        return userStorage.update(user).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + user.getId() + " не существует"));
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(int id) {
        return userStorage.get(id).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + id + " не существует"));
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        ;
        User friend = userStorage.get(friendId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + friendId + " не существует"));
        ;
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователи id={} и id={} добавлены в друзья", userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        ;
        User friend = userStorage.get(friendId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + friendId + " не существует"));
        ;
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователи id={} и id={} удалены из друзей", userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        ;

        return user.getFriends().stream()
                .map(userStorage::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.get(userId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + userId + " не существует"));
        ;
        User other = userStorage.get(otherId).orElseThrow(()
                -> new NotFoundException("Пользователя с id=" + otherId + " не существует"));
        ;

        return user.getFriends().stream()
                .filter(id -> other.getFriends().contains(id))
                .map(userStorage::get)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

}
