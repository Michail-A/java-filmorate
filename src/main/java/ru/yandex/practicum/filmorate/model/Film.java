package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Integer> likes = new HashSet<>();

    public void addLikes(List<Integer> likesDb){
        for (Integer like : likesDb) {
            likes.add(like);
        }
    }
}
