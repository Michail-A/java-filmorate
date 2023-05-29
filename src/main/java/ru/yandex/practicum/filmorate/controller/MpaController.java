package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.impl.MpaDbStorage;

import java.util.List;

@RestController
@RequestMapping(path = "/mpa")
public class MpaController {

    private static final Logger log = LoggerFactory.getLogger(MpaController.class);
    private final MpaStorage mpaDbStorage;

    @Autowired
    public MpaController(MpaStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    public List<Mpa> getMpa() {
        log.info("Получен запрос на получение списка рейтингов");
        return mpaDbStorage.getMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaForId(@PathVariable int id) {
        log.info("Получен запрос на получение рейтинга id= " + id);
        return mpaDbStorage.getMpaForId(id);
    }
}
