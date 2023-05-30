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
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest()
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
        mpa = mpaStorage.getMpaForId(1);
        film = new Film("Test", "Test", LocalDate.of(2020, 02, 01),
                60, mpa);
        filmUp = new Film("TestUp", "TestUp", LocalDate.of(2020, 02, 01),
                60, mpa);
        user = new User("test@test.ru", "testlog", LocalDate.of(1900, 02, 03));
        user.setName("Tester");
        userUp = new User("supertest@test.ru", "testlogup", LocalDate.of(1900, 02, 03));
        userUp.setName("supertester");
        userStorage.addUser(user);
        userStorage.addUser(userUp);
    }

    @Test
    public void getGenres() {
        assertEquals(genreStorage.getGenres().size(), 6);
    }

    @Test
    public void testGetGenreById() {
        assertEquals(genreStorage.getGenreForId(1).getName(), "Комедия");
        assertEquals(genreStorage.getGenreForId(2).getName(), "Драма");
    }

    @Test
    void testGetMpas() {
        assertEquals(mpaStorage.getMpa().size(), 5);
    }

    @Test
    void testGetMpaById() {
        assertEquals(mpaStorage.getMpaForId(1).getName(), "G");
        assertEquals(mpaStorage.getMpaForId(2).getName(), "PG");
    }

    @Test
    void getFilms() {
        filmStorage.addFilm(film);
        filmStorage.addFilm(filmUp);
        List<Film> films = filmStorage.getFilms();
        assertEquals(films.size(), 2);
    }

    @Test
    void addFilm() {
        Film filmAdded = filmStorage.addFilm(film);
        film.setId(1);
        assertEquals(filmAdded, film);
    }

    @Test
    void updateFilm() {
        filmUp.setId(1);
        filmStorage.addFilm(film);
        assertEquals(filmStorage.updateFilm(filmUp), filmUp);
    }

    @Test
    void getFilmForId() {
        filmStorage.addFilm(film);
        film.setId(1);
        assertEquals(filmStorage.getFilmForId(1), film);
    }

    @Test
    void addLike() {
        filmStorage.addFilm(film);
        filmStorage.addLike(1, 1);
        assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 1);
    }

    @Test
    void deleteLike() {
        filmStorage.addFilm(film);
        filmStorage.addLike(1, 1);
        assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 1);
        filmStorage.deleteLike(1, 1);
        assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 0);
    }

    @Test
    void addUser() {
        User userAdded = userStorage.addUser(user);
        user.setId(1);
        assertEquals(userAdded, user);
    }

    @Test
    void updateUser() {
        userUp.setId(1);
        userStorage.addUser(user);
        assertEquals(userStorage.updateUser(userUp), userUp);

    }

    @Test
    void getUsers() {
        assertEquals(userStorage.getUsers().size(), 2);
    }

    @Test
    void getUserForId() {
        assertEquals(userStorage.getUserForId(1), user);
    }

    @Test
    void addFriend() {
        userStorage.addFriend(1, 2);
        assertEquals(userStorage.getFriends(1).size(), 1);
    }

    @Test
    void deleteFriend() {
        userStorage.deleteFriend(1, 2);
        assertEquals(userStorage.getFriends(1).size(), 0);
    }

    @Test
    void getFriends() {
        userStorage.addFriend(1, 2);
        List<User> friends = userStorage.getFriends(1);
        User friend = friends.get(0);
        assertEquals(friend, userUp);
    }
}