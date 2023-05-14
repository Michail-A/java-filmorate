package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.Exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void uncorrectedEmail() {
        user = new User("mail.ru", "test", LocalDate.of(1999, 04, 28));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Некорректный email", exception.getMessage());
        assertEquals(userController.getUsers().size(), 0);
    }

    @Test
    void uncorrectedLogin() {
        user = new User("test@mail.ru", "", LocalDate.of(1999, 04, 28));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Некорректный логин", exception.getMessage());
        assertEquals(userController.getUsers().size(), 0);

        user = new User("test@mail.ru", "t e s t", LocalDate.of(1999, 04, 28));
        assertEquals("Некорректный логин", exception.getMessage());
        assertEquals(userController.getUsers().size(), 0);
    }

    @Test
    void uncorrectedBirthday(){
        user = new User("test@mail.ru", "test", LocalDate.of(2100, 04, 28));
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Некорректная дата рождения", exception.getMessage());
        assertEquals(userController.getUsers().size(), 0);
    }
}
