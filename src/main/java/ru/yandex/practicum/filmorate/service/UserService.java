package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendsId) {
        if (!userStorage.getIdUsers().contains(userId)) {
            throw new ObjectNotFoundException("Пользователь id= " + userId + " не найден");
        }
        if (!userStorage.getIdUsers().contains(friendsId)) {
            throw new ObjectNotFoundException("Пользователь id= " + friendsId + " не найден");
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
        Set<Integer> friends = userStorage.getUserForId(friendsId).getFriends();
        for (Integer id : userFriends) {
            if (friends.contains(id)) {
                commonFriends.add(userStorage.getUserForId(id));
            }
        }
        return commonFriends;
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        Set<Integer> friendsId = userStorage.getUserForId(id).getFriends();
        for (Integer friendId : friendsId) {
            friends.add(userStorage.getUserForId(friendId));
        }
        return friends;
    }
}
