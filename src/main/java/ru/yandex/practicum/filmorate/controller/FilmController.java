package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmValidateService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RestController
@RequestMapping("/films")
public class FilmController {
    FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

}