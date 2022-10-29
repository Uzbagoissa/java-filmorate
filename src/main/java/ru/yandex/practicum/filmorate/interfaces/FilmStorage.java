package ru.yandex.practicum.filmorate.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {//
    Film getFilm(Integer id);
    HashMap<Integer, Film> getFilms();
    List<Film> getAllFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
