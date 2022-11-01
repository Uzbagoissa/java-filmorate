package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService implements Comparator<Film> {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmValidateServiceStorage filmValidServiceStorage;
    private final UserValidateServiceStorage userValidateServiceStorage;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                       @Qualifier("inMemoryUserStorage") UserStorage userStorage,
                       FilmValidateServiceStorage filmValidServiceStorage,
                       UserValidateServiceStorage userValidateServiceStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmValidServiceStorage = filmValidServiceStorage;
        this.userValidateServiceStorage = userValidateServiceStorage;
    }

    public List<Film> getMostPopularFilms (Integer count) {
        return filmStorage.getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film addLike (Integer id, Integer userId) {
        filmValidServiceStorage.checkFilmValidate(log, filmStorage.getFilms(), id);
        userValidateServiceStorage.checkUserValidate(log, userStorage.getUsers(), id);
        filmValidServiceStorage.checkAddLikeValidate(log, filmStorage, id, userId);
        filmStorage.getFilms().get(id).getLikes().add(userId);
        return filmStorage.getFilms().get(id);
    }

    public Film removeLike (Integer id, Integer userId) {
        filmValidServiceStorage.checkFilmValidate(log, filmStorage.getFilms(), id);
        userValidateServiceStorage.checkUserValidate(log, userStorage.getUsers(), id);
        filmValidServiceStorage.checkRemoveLikeValidate(log, filmStorage, id, userId);
        filmStorage.getFilms().get(id).getLikes().remove(userId);
        return filmStorage.getFilms().get(id);
    }

    @Override
    public int compare(Film f0, Film f1) {
        int result = Integer.compare(f1.getLikes().size(), f0.getLikes().size());
        return result;
    }
}
