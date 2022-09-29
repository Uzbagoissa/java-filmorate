package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int filmID = 1;

    @GetMapping()
    public ArrayList<Film> getFilms() {
        ArrayList<Film> filmsValue = new ArrayList<>();
        for (Film value : films.values()) {
            filmsValue.add(value);
        }
        return filmsValue;
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws FilmAlreadyExistException, InvalidFilmNameException, InvalidDescriptionException, InvalidReleaseDateException, InvalidDurationException {
        if (films.containsKey(film.getId())){
            log.error("Фильм уже был добавлен!, {}", film);
            throw new FilmAlreadyExistException("Фильм уже был добавлен!");
        } else if (film.getName().trim().equals("")){
            log.error("Название не может быть пустым!, {}", film);
            throw new InvalidFilmNameException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200){
            log.error("Максимальная длина описания — 200 символов!, {}", film);
            throw new InvalidDescriptionException("Максимальная длина описания — 200 символов!");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))){
            log.error("Дата релиза не должна быть раньше 28 декабря 1895!, {}", film);
            throw new InvalidReleaseDateException("Дата релиза не должна быть раньше 28 декабря 1895!");
        } else if (film.getDuration() <= 0){
            log.error("Продолжительность фильма должна быть положительна!, {}", film);
            throw new InvalidDurationException("Продолжительность фильма должна быть положительна!");
        } else {
            film.setId(filmID);
            films.put(filmID, film);
            filmID = filmID + 1;
            log.info("Добавлен новый фильм, {}", film);
            return film;
        }
    }

    @PutMapping()
    public Film renewFilm(@RequestBody Film film) throws InvalidFilmException {
        if (!films.containsKey(film.getId())){
            log.error("Такого фильма не существует!, {}", film);
            throw new InvalidFilmException("Такого фильма не существует!");
        } else {
            films.remove(film);
            film.setId(film.getId());
            films.put(filmID, film);
            log.info("Фильм обновлен - , {}", film);
            return film;
        }
    }
}