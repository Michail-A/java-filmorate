package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.NotSpacesCustom;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {


    private Integer id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @NotSpacesCustom
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    Set<Integer> friends = new HashSet<>();

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
