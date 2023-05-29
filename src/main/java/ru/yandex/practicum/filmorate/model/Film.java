package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private final Mpa mpa;
    private final List<Genre> genres;
    private Set<Integer> likes = new HashSet<>();

    public void addLikes(List<Integer> likesDb) {
        likes.addAll(likesDb);
    }
}
