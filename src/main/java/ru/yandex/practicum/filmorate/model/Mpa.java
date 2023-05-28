package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Mpa {
    @EqualsAndHashCode.Exclude
    private int id;
    private String name;
}
