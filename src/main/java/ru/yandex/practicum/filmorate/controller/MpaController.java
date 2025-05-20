package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
@Slf4j
public class MpaController {

    private final FilmService filmService;

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Получен запрос на получение списка рейтингов");
        return filmService.getMpaAll();
    }

    @GetMapping("/{id}")
    public Mpa get(@PathVariable int id) {
        log.info("Получен запрос на получение рейтинга id= {}", id);
        return filmService.getMpa(id);
    }
}
