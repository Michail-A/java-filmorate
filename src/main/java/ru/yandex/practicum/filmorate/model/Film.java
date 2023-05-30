package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private final Mpa mpa;
    private List<Genre> genres;
    private Set<Integer> likes;

    public void addLikes(int like) {
        likes.add(like);
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
