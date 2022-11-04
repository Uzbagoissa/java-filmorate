package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Service
public class FilmValidateServiceDb {
    public void checkFilmValidate(Logger log, SqlRowSet userRows, Integer id) {
        if(!userRows.next()) {
            log.error("Такого фильма не существует!, {}", id);
            throw new NotFoundException("Такого фильма не существует!");
        }
    }

    public void checkGenreValidate(Logger log, SqlRowSet userRows, Integer id) {
        if(!userRows.next()) {
            log.error("Такого жанра не существует!, {}", id);
            throw new NotFoundException("Такого жанра не существует!");
        }
    }

    public void checkRatingMPAValidate(Logger log, SqlRowSet userRows, Integer id) {
        if(!userRows.next()) {
            log.error("Такого рейтинга не существует!, {}", id);
            throw new NotFoundException("Такого рейтинга не существует!");
        }
    }

    public void checkAddFilmValidate(Logger log, SqlRowSet userRows, Film film) {
        if (userRows.next()) {
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
        } else if (film.getMpa().getId() <= 0 || film.getMpa().getId() > 5) {
            log.error("Индекс рейтинг фильма должен быть в пределах от 1 до 5!, {}", film);
            throw new ValidationException("Индекс рейтинг фильма должен быть в пределах от 1 до 5!");
        }
        for (int i = 0; i < film.getGenres().size(); i++) {
            if (film.getGenres().get(i).getId() <= 0 || film.getGenres().get(i).getId() > 6) {
                log.error("Индекс жанра фильма должен быть в пределах от 1 до 6!, {}", film);
                throw new ValidationException("Индекс жанра фильма должен быть в пределах от 1 до 6!");
            }
        }
    }
}
