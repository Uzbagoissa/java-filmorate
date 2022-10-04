package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidateService {
    private static final Logger log = LoggerFactory.getLogger(FilmValidateService.class);

    public void checkPUTFilmValidate(Film film) {
        if (!FilmController.FILMS.containsKey(film.getId())) {
            log.error("Такого фильма не существует!, {}", film);
            throw new ValidationException("Такого фильма не существует!");
        }
    }

    public void checkPOSTFilmValidate(Film film) {
        if (FilmController.FILMS.containsKey(film.getId())) {
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
