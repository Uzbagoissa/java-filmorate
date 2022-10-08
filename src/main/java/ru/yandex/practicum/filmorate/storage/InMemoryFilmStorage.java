package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private static final HashMap<Integer, Film> films = new HashMap<>();
    private int filmID = 1;
    private FilmValidateService filmValidService = new FilmValidateService();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        filmValidService.checkPOSTFilmValidate(log, films, film);
        film.setId(filmID);
        films.put(filmID, film);
        filmID++;
        log.info("Добавлен новый фильм, {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidService.checkPUTFilmValidate(log, films, film);
        film.setId(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм обновлен - , {}", film);
        return film;
    }
}
