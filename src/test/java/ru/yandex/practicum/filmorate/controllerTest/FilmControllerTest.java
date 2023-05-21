package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController;
    Film film;

    FilmService filmService;
    InMemoryFilmStorage filmStorage;

    @BeforeEach
    void beforeEach() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage);
        filmController = new FilmController(filmStorage, filmService);
    }

    @Test
    void emptyName() {
        film = new Film("", "Test", LocalDate.of(2020, 02, 01), 90);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Название не может быть пустым", exception.getMessage());
        assertEquals(filmController.getFilms().size(), 0);
    }

    @Test
    void longDescription() {
        film = new Film("Test", "Test".repeat(1000) +
                "", LocalDate.of(2020, 02, 01), 90);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Описание больше 200 символов!", exception.getMessage());
        assertEquals(filmController.getFilms().size(), 0);
    }

    @Test
    void uncorrectedDate() {
        film = new Film("Test", "Test", LocalDate.of(1895, 12, 27), 90);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Дата не может быть раньше 28.12.1895", exception.getMessage());
        assertEquals(filmController.getFilms().size(), 0);
    }

    @Test
    void negativeDuration() {
        film = new Film("Test", "Test", LocalDate.of(2020, 02, 01), -90);
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Продолжительность не может быть отрицательной", exception.getMessage());
        assertEquals(filmController.getFilms().size(), 0);
    }
}
