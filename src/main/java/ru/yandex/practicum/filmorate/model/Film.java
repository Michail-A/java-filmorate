package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    public Set<Integer> likes = new HashSet<>();

    public void addLike(int id){
        likes.add(id);
    }

    public void deleteLike(int id){
        likes.remove(id);
    }
}
