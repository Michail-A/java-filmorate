package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Genre {
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
}
