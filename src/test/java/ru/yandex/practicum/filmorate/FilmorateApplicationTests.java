package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    Film film;
    Film filmUp;

    User user;
    User userUp;
    Mpa mpa;

    @BeforeEach
    void beforeEach() {
        mpa = mpaStorage.get(1).get();
        film = new Film(1, "Test", "Test", LocalDate.of(2020, 02, 01),
                60, new HashSet<>(), new ArrayList<>(), mpa);
        filmUp = new Film(1, "SuperTest", "Test", LocalDate.of(2020, 02, 01),
                60, new HashSet<>(), new ArrayList<>(), mpa);
        user = new User("test@test.ru", "testlog", LocalDate.of(1900, 02, 03));
        userUp = new User("supertest@test.ru", "testlogup", LocalDate.of(1900, 02, 03));
    }

    @Test
    public void getGenres() {
        assertEquals(genreStorage.getAll().size(), 6);
    }

    @Test
    public void testGetGenreById() {
        assertEquals(genreStorage.get(1).get().getName(), "Комедия");
        assertEquals(genreStorage.get(2).get().getName(), "Драма");
    }

    @Test
    void testGetMpas() {
        assertEquals(mpaStorage.getAll().size(), 5);
    }

    @Test
    void testGetMpaById() {
        assertEquals(mpaStorage.get(1).get().getName(), "G");
        assertEquals(mpaStorage.get(2).get().getName(), "PG");
    }

    @Test
    void getFilms() {
        filmStorage.add(film);
        filmStorage.add(filmUp);

        List<Film> films = filmStorage.getAll();

        assertEquals(films.size(), 2);
    }

    @Test
    void addFilm() {
        Film filmAdded = filmStorage.add(film).get();
        film.setId(1);

        assertEquals(filmAdded, film);
    }

    @Test
    void updateFilm() {
        filmUp.setId(1);
        filmStorage.add(film);

        assertEquals(filmStorage.update(filmUp).get(), filmUp);
    }

    @Test
    void getFilmForId() {
        filmStorage.add(film);
        film.setId(1);
        assertEquals(filmStorage.get(1).get(), film);
    }

    @Test
    void addLike() {
        filmStorage.add(film);
        userStorage.add(user);

        filmStorage.addLike(1, 1);

        assertEquals(filmStorage.get(1).get().getLikes().size(), 1);
    }

    @Test
    void deleteLike() {
        filmStorage.add(film);
        userStorage.add(user);

        filmStorage.addLike(1, 1);

        assertEquals(filmStorage.get(1).get().getLikes().size(), 1);

        filmStorage.deleteLike(1, 1);
        assertEquals(filmStorage.get(1).get().getLikes().size(), 0);
    }

    @Test
    void addUser() {
        User userAdded = userStorage.add(user).get();

        user.setId(1);
        assertEquals(userAdded, user);
    }

    @Test
    void updateUser() {
        userStorage.add(user);
        userUp.setId(1);

        User updatedUser = userStorage.update(userUp).get();

        assertEquals(updatedUser, userUp);

    }

    @Test
    void getUsers() {
        userStorage.add(user);
        userStorage.add(userUp);

        assertEquals(userStorage.getAll().size(), 2);
    }

    @Test
    void getUserForId() {
        userStorage.add(user);
        user.setId(1);

        assertEquals(userStorage.get(1).get(), user);
    }

    @Test
    void addFriend() {
        userStorage.add(user);
        userStorage.add(userUp);

        userStorage.addFriend(1, 2);

        assertEquals(userStorage.getFriends(1).size(), 1);
    }

    @Test
    void deleteFriend() {
        userStorage.add(user);
        userStorage.add(userUp);
        userStorage.addFriend(1, 2);

        userStorage.deleteFriend(1, 2);

        assertEquals(userStorage.getFriends(1).size(), 0);
    }

    @Test
    void getFriends() {
        userStorage.add(user);
        userStorage.add(userUp);

        userStorage.addFriend(1, 2);
        List<User> friends = userStorage.getFriends(1);

        User friend = friends.get(0);
        assertEquals(friend, userStorage.get(2).get());
    }

}
