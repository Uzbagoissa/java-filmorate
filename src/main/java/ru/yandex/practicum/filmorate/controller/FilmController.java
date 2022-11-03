package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("filmDbStorage") FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return filmStorage.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms (@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count < 0) {
            throw new NotFoundException("count");
        }
        return filmService.getMostPopularFilms(count);
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

    @GetMapping("/mpa/{id}")
    public RatingMPA getRatingMPA(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return filmStorage.getRatingMPA(id);
    }

    @GetMapping("/mpa")
    public List<RatingMPA> getRatingMPAs() {
        return filmStorage.getRatingMPAs();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) {
        return filmStorage.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
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

    @DeleteMapping("/{id}/like/{userId}")
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

}