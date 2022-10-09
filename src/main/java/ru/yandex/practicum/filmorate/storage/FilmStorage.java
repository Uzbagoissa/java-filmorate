package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface FilmStorage {
    Film getFilm(Integer id);
    HashMap<Integer, Film> getFilms();
    List<Film> getAllFilms();
    Film addFilm(Film film);
    Film updateFilm(Film film);
}
