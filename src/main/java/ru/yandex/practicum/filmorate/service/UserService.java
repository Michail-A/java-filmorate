package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendsId) {
        if (!userStorage.getIdUsers().contains(userId) || !userStorage.getIdUsers().contains(friendsId)) {
            throw new RuntimeException();
        }
        User user = userStorage.getUserForId(userId);
        user.addFriend(friendsId);
        userStorage.updateUser(user);
        user = userStorage.getUserForId(friendsId);
        user.addFriend(userId);
        userStorage.updateUser(user);
        log.info(userId + " добавил в друзья " + friendsId);
    }

    public void deleteFriend(int userId, int friendsId) {
        User user = userStorage.getUserForId(userId);
        user.deleteFriend(friendsId);
        userStorage.updateUser(user);
        user = userStorage.getUserForId(friendsId);
        user.deleteFriend(userId);
        userStorage.updateUser(user);
        log.info(userId + " удалил из друзей " + friendsId);
    }

    public List<User> getCommonFriends(int userId, int friendsId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Integer> userFriends = userStorage.getUserForId(userId).getFriends();
        for (Integer id : userFriends) {
            Set<Integer> friends = userStorage.getUserForId(friendsId).getFriends();
            if (friends.contains(id)) {
                commonFriends.add(userStorage.getUserForId(id));
            }
        }
        return commonFriends;
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendId : userStorage.getUserForId(id).friends) {
            friends.add(userStorage.getUserForId(friendId));
        }
        return friends;
    }
}
