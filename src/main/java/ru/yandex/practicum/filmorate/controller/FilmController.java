package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashSet<Film> films = new HashSet<>();

    @GetMapping()
    public HashSet<Film> getFilms() {
        return films;
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws FilmAlreadyExistException {
        if (films.contains(film)){
            throw new FilmAlreadyExistException("Фильм уже был добавлен!");
        }  else {
            films.add(film);
            return film;
        }
    }

    @PutMapping()
    public Film renewFilm(@RequestBody Film film) {
        films.add(film);
        return film;
    }
}