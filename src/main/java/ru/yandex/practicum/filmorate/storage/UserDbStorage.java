package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

@Repository
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User getUserForId(int id) {
        return null;
    }

    @Override
    public Set<Integer> getIdUsers() {
        return null;
    }
}
