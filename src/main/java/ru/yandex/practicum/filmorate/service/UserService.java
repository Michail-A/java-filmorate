package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(int userId, int friendsId){
        if(!userStorage.getIdUsers().contains(userId) || !userStorage.getIdUsers().contains(friendsId)){
            throw new RuntimeException();
        }
        User user = userStorage.getUserForId(userId);
        user.addFriend(friendsId);
        userStorage.updateUser(user);
        user = userStorage.getUserForId(friendsId);
        user.addFriend(userId);
        userStorage.updateUser(user);
        return userId + " добавил в друзья " + friendsId;
    }

    public String deleteFriend(int userId, int friendsId){
        User user = userStorage.getUserForId(userId);
        user.deleteFriend(friendsId);
        userStorage.updateUser(user);
        user = userStorage.getUserForId(friendsId);
        user.deleteFriend(userId);
        userStorage.updateUser(user);
        return userId + " удалил из друзей " +friendsId;
    }

    public List<Integer> getCommonFriends(int userId, int friendsId){
        List<Integer> commonFriends = new ArrayList<>();
        Set<Integer> userFriends = userStorage.getUserForId(userId).getFriends();
        for (Integer id : userFriends) {
            if(userStorage.getUserForId(friendsId).getFriends().contains(id)){
                commonFriends.add(id);
            }
        }
        return commonFriends;
    }
}
