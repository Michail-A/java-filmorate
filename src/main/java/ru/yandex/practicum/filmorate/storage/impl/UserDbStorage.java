package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
@Primary
public class UserDbStorage implements UserStorage {
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User addUser(User user) {
        validate(user);
        String sqlQuery = "insert into users(email, login, name, birthday) values (?, ?, ?, ?)";
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validate(user);
        String sqlQuery = "update users set " +
                "email = ?, login = ?, name = ?, birthday = ?" +
                "where id = ?";
        try {
            jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(),
                    user.getBirthday(), user.getId());
            return getUserForId(user.getId());
        } catch (RuntimeException e) {
            throw new ObjectNotFoundException("Пользователь id=" + user.getId() + " не найден");
        }
    }

    @Override
    public List<User> getUsers() {
        String sqlQuery = "select * from users";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum));
    }

    @Override
    public User getUserForId(int id) {
        try {
            String sql = "select * from users where id=?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeUser(rs, rowNum), id);
        } catch (RuntimeException e) {
            throw new ObjectNotFoundException("Пользователь id=" + id + " не найден");
        }
    }

    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "insert into friends (user_id, friend_id) values (?, ?)";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (RuntimeException e) {
            throw new ObjectNotFoundException("Ошибка в id=" + userId + " friendId=" + friendId);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int id) {
        String sqlQuery = "select users.id, users.email, users.LOGIN , users.name,users.BIRTHDAY  from users " +
                "inner join friends on users.id=friends.friend_id where friends.user_id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeUser(rs, rowNum), id);
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        User user = new User(email, login, birthday);
        user.setId(id);
        user.setName(name);
        return user;
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
