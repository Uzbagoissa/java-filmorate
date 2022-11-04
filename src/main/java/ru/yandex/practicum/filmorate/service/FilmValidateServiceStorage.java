package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class FilmValidateServiceStorage {

    public void checkRemoveLikeValidate(Logger log, FilmStorage filmStorage, Integer id, Integer userId) {
        if (!filmStorage.getFilms().get(id).getLikes().contains(userId)){
            log.error("Этот пользователь не ставил лайк этому фильму, {}", userId);
            throw new ValidationException("Этот пользователь не ставил лайк этому фильму");
        }
    }

    public void checkAddLikeValidate(Logger log, FilmStorage filmStorage, Integer id, Integer userId) {
        if (filmStorage.getFilms().get(id).getLikes().contains(userId)){
            log.error("Лайк этого пользователя уже есть у фильма, {}", userId);
            throw new ValidationException("Лайк этого пользователя уже есть у фильма");
        }
    }

    public void checkFilmValidate(Logger log, HashMap<Integer, Film> films, Integer id) {
        if (!films.containsKey(id)){
            log.error("Такого фильма не существует!, {}", id);
            throw new NotFoundException("Такого фильма не существует!");
        }
    }

    public void checkAddFilmValidate(Logger log, HashMap<Integer, Film> films, Film film) {
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
        } else if (film.getMpa().getId() <= 0 || film.getMpa().getId() > 5) {
            log.error("Индекс рейтинга фильма должен быть в пределах от 1 до 5!, {}", film);
            throw new ValidationException("Индекс рейтинга фильма должен быть в пределах от 1 до 5!");
        }
        for (int i = 0; i < film.getGenres().size(); i++) {
            if (film.getGenres().get(i).getId() <= 0 || film.getGenres().get(i).getId() > 6) {
                log.error("Индекс жанра фильма должен быть в пределах от 1 до 6!, {}", film);
                throw new ValidationException("Индекс жанра фильма должен быть в пределах от 1 до 6!");
            }
        }
    }
}
