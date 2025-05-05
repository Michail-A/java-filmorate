package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NotSpacesCustom;

import java.time.LocalDate;

@Data
public class User {


    private int id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @NotSpacesCustom
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
