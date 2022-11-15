package ru.yandex.practicum.filmorate.storage.storageInterfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    Film getFilm(Integer id);
    List<Genre> getGenres();
    Genre getGenre(Integer id);
    Mpa getMPA(Integer id);
    List<Mpa> getMPAs();
    HashMap<Integer, Film> getFilms();
    List<Film> getAllFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
