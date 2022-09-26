package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public Film addFilm(@RequestBody Film film) throws FilmAlreadyExistException, InvalidFilmNameException, InvalidDescriptionException, InvalidReleaseDateException, InvalidDurationException {
        if (films.contains(film)){
            throw new FilmAlreadyExistException("Фильм уже был добавлен!");
        } else if (film.getName().trim().equals("")){
            throw new InvalidFilmNameException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200){
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов!");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new InvalidReleaseDateException("Дата релиза не должна быть раньше 28 декабря 1895!");
        } else if (film.getDuration() <= 0){
            throw new InvalidDurationException("Продолжительность фильма должна быть положительна!");
        } else {
            films.add(film);
            return film;
        }
    }

    @PutMapping()
    public Film renewFilm(@RequestBody Film film) throws InvalidFilmNameException, InvalidDescriptionException, InvalidReleaseDateException, InvalidDurationException {
        if (film.getName().trim().equals("")){
            throw new InvalidFilmNameException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200){
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов!");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            throw new InvalidReleaseDateException("Дата релиза не должна быть раньше 28 декабря 1895!");
        } else if (film.getDuration() <= 0){
            throw new InvalidDurationException("Продолжительность фильма должна быть положительна!");
        } else {
            films.remove(film);
            films.add(film);
            return film;
        }
    }
}