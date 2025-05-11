package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User add(User user) {
        if (user.getName() == null) {
            log.info("У пользователя не указанно имя, в качестве имени присвоен логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    @Override
    public User update(User user) {
        return userStorage.update(user);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(int id) {
        return userStorage.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователи id={} и id={} добавлены в друзья", userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователи id={} и id={} удалены из друзей", userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = userStorage.get(userId);
        return user.getFriends().stream()
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        User user = userStorage.get(userId);
        User other = userStorage.get(otherId);

        return user.getFriends().stream()
                .filter(id -> other.getFriends().contains(id))
                .map(userStorage::get)
                .collect(Collectors.toList());
    }

}
