package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashMap;

public class FilmValidateService {

    public void checkFilmValidateElse(FilmStorage filmStorage, Integer id) {
        if (!filmStorage.getFilms().containsKey(id)){
            throw new NotFoundException("Такого фильма не существует!");
        }
    }

    public void checkFilmValidate(Logger log, HashMap<Integer, Film> films, Integer id) {
        if (!films.containsKey(id)){
            log.error("Такого фильма не существует!, {}", id);
            throw new NotFoundException("Такого фильма не существует!");
        }
    }

    public void checkPUTFilmValidate(Logger log, HashMap<Integer, Film> films, Film film) {
        if (!films.containsKey(film.getId())) {
            log.error("Такого фильма не существует!, {}", film);
            throw new NotFoundException("Такого фильма не существует!");
        }
    }

    public void checkPOSTFilmValidate(Logger log, HashMap<Integer, Film> films, Film film) {
        if (films.containsKey(film.getId())) {
            log.error("Фильм уже был добавлен!, {}", film);
            throw new ValidationException("Фильм уже был добавлен!");
        } else if (film.getName().isBlank()) {
            log.error("Название не может быть пустым!, {}", film);
            throw new ValidationException("Название не может быть пустым!");
        } else if (film.getDescription().length() > 200) {
            log.error("Максимальная длина описания — 200 символов!, {}", film);
            throw new ValidationException("Максимальная длина описания — 200 символов!");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Дата релиза не должна быть раньше 28 декабря 1895!, {}", film);
            throw new ValidationException("Дата релиза не должна быть раньше 28 декабря 1895!");
        } else if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительна!, {}", film);
            throw new ValidationException("Продолжительность фильма должна быть положительна!");
        }
    }
}
