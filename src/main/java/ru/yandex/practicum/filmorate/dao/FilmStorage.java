package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    Film getFilm(Integer id);

    List<Genre> getGenres();

    Genre getGenre(Integer id);

    RatingMPA getRatingMPA(Integer id);

    List<RatingMPA> getRatingMPAs();

    HashMap<Integer, Film> getFilms();
    List<Film> getAllFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
