package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    Optional<User> add(User user);

    Optional<User> update(User user);

    List<User> getAll();

    Optional<User> get(int id);
}
