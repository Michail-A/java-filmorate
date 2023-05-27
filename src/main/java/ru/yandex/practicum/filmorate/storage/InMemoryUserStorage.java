package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        validate(user);
        user.setId(userId);
        users.put(user.getId(), user);
        userId++;
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Пользователь id=" + user.getId() + " не найден");
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserForId(int id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь id=" + id + " не найден");
        }
        return users.get(id);
    }

    private void validate(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Некорректный email");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Некорректный логин");
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Некорректная дата рождения");
        }
    }
}
