package ru.yandex.practicum.filmorate.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Genre {
    private int id;
    private String name;
}
