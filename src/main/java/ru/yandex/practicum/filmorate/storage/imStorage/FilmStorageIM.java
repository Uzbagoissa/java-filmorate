package ru.yandex.practicum.filmorate.storage.imStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validate.imValidate.FilmValidateIM;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FilmStorageIM implements FilmStorage {
    private final FilmValidateIM filmValidateIM;
    private static final HashMap<Integer, Film> films = new HashMap<>();
    private int filmID = 1;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @Autowired
    public FilmStorageIM(FilmValidateIM filmValidateIM) {
        this.filmValidateIM = filmValidateIM;
    }

    @Override
    public Film getFilm(Integer id) {
        filmValidateIM.checkFilmValidate(log, films, id);
        log.info("Найден фильм");
        return films.get(id);
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return films;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Найдены фильмы");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        filmValidateIM.checkAddFilmValidate(log, films, film);
        film.setId(filmID);
        films.put(filmID, film);
        filmID++;
        log.info("Добавлен новый фильм, {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        filmValidateIM.checkFilmValidate(log, films, film.getId());
        film.setId(film.getId());
        films.put(film.getId(), film);
        log.info("Фильм обновлен - , {}", film);
        return film;
    }



    @Override
    public List<Genre> getGenres() {
        return null;
    }

    @Override
    public Genre getGenre(Integer id) {
        return null;
    }

    @Override
    public Mpa getMPA(Integer id) {
        return null;
    }

    @Override
    public List<Mpa> getMPAs() {
        return null;
    }

}
