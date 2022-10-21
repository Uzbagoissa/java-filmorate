package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class InMemoryFilmStorage implements FilmStorage {
    private final FilmValidateService filmValidService;
    private static final HashMap<Integer, Film> films = new HashMap<>();
    private int filmID = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public InMemoryFilmStorage(FilmValidateService filmValidService) {
        this.filmValidService = filmValidService;
    }

    @Override
    public Film getFilm(Integer id) {
        filmValidService.checkFilmValidate(log, films, id);
        return films.get(id);
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        filmValidService.checkAddFilmValidate(log, films, film);
        film.setId(filmID);
        films.put(filmID, film);
        filmID++;
        log.info("Добавлен новый фильм, {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidService.checkFilmValidate(log, films, film.getId());
        film.setId(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм обновлен - , {}", film);
        return film;
    }
}
