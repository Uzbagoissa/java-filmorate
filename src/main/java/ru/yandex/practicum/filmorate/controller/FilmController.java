package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    FilmValidateService filmValidService = new FilmValidateService();
    public static final HashMap<Integer, Film> FILMS = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int filmID = 1;

    @GetMapping()
    public List<Film> getAllFilms() {
        return new ArrayList<>(FILMS.values());
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        filmValidService.checkPOSTFilmValidate(film);
        film.setId(filmID);
        FILMS.put(filmID, film);
        filmID++;
        log.info("Добавлен новый фильм, {}", film);
        return film;
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        filmValidService.checkPUTFilmValidate(film);
        film.setId(film.getId());
        FILMS.put(film.getId(), film);
        log.info("Фильм обновлен - , {}", film);
        return film;
    }

}