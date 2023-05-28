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


    public List<User> getCommonFriends(int userId, int friendsId) {
        List<User> commonFriends = new ArrayList<>();
        List<User> userFriends = userStorage.getFriends(userId);
        List<User> friends = userStorage.getFriends(friendsId);;
        for (User user: userFriends) {
            if (friends.contains(user)) {
                commonFriends.add(user);
            }
        }
        return commonFriends;
    }
}