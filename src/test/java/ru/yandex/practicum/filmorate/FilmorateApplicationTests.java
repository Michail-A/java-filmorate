package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.constraints.Max;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeEach
    void beforeEach() {
        film = new Film("Test", "Test", LocalDate.of(2020, 02, 01), 60);
        film.setId(1);
        film.setMpa(mpaStorage.getMpaForId(1));
        filmUp = new Film("TestUp", "TestUp", LocalDate.of(2020, 02, 01), 60);
        filmUp.setId(1);
        filmUp.setMpa(mpaStorage.getMpaForId(1));

        user = new User("test@test.ru", "test", LocalDate.of(1900, 02, 03));
        user.setId(1);
        user.setName("Tester");
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
        List<Film> films = filmStorage.getFilms();
        assertEquals(films.size(), 1);
        assertEquals(films.get(0), film);

    }

    @Test
    void addFilm() {
        assertEquals(filmStorage.addFilm(film), film);
    }

    @Test
    void updateFilm() {
        filmStorage.addFilm(film);
        assertEquals(filmStorage.updateFilm(filmUp), filmUp);
    }

    @Test
    void getFilmForId() {
        filmStorage.addFilm(film);
        assertEquals(filmStorage.getFilmForId(1), film);
    }

    @Test
    void addLike() {
		userStorage.addUser(user);
		filmStorage.addLike(1,1);
		assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 1);
    }

    @Test
    void deleteLike() {
        userStorage.addUser(user);
		filmStorage.addLike(1,1);
		assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 1);
		filmStorage.deleteLike(1, 1);
		assertEquals(filmStorage.getFilmForId(1).getLikes().size(), 0);
    }

}