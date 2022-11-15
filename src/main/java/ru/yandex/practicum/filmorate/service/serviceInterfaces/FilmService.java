package ru.yandex.practicum.filmorate.service.serviceInterfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    List<Film> getMostPopularFilms (Integer count);
    Film addLike (Integer id, Integer userId);
    Film removeLike (Integer id, Integer userId);
}
