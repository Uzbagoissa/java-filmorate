package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.FilmService;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RestController
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmStorageDB") FilmStorage filmStorage, @Qualifier("filmServiceDB") FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return filmStorage.getFilm(id);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms (@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count < 0) {
            throw new NotFoundException("count");
        }
        return filmService.getMostPopularFilms(count);
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLike (@PathVariable("id") Integer id,
                         @PathVariable("userId") Integer userId) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        if (userId <= 0) {
            throw new NotFoundException("userId");
        }
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film removeLike (@PathVariable("id") Integer id,
                            @PathVariable("userId") Integer userId) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        if (userId <= 0) {
            throw new NotFoundException("userId");
        }
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMPA(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return filmStorage.getMPA(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMPAs() {
        return filmStorage.getMPAs();
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return filmStorage.getGenre(id);
    }

}