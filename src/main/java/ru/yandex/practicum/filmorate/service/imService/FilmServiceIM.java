package ru.yandex.practicum.filmorate.service.imService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.UserStorage;
import ru.yandex.practicum.filmorate.validate.imValidate.FilmValidateIM;
import ru.yandex.practicum.filmorate.validate.imValidate.UserValidateIM;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceIM implements FilmService, Comparator<Film> {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmValidateIM filmValidateIM;
    private final UserValidateIM userValidateIM;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public FilmServiceIM(@Qualifier("filmStorageIM") FilmStorage filmStorage,
                         @Qualifier("userStorageIM") UserStorage userStorage,
                         FilmValidateIM filmValidateIM,
                         UserValidateIM userValidateIM) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmValidateIM = filmValidateIM;
        this.userValidateIM = userValidateIM;
    }

    @Override
    public List<Film> getMostPopularFilms (Integer count) {
        return filmStorage.getFilms().values().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film addLike (Integer id, Integer userId) {
        filmValidateIM.checkFilmValidate(log, filmStorage.getFilms(), id);
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        filmValidateIM.checkAddLikeValidate(log, filmStorage, id, userId);
        filmStorage.getFilms().get(id).getLikes().add(userId);
        return filmStorage.getFilms().get(id);
    }

    @Override
    public Film removeLike (Integer id, Integer userId) {
        filmValidateIM.checkFilmValidate(log, filmStorage.getFilms(), id);
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        filmValidateIM.checkRemoveLikeValidate(log, filmStorage, id, userId);
        filmStorage.getFilms().get(id).getLikes().remove(userId);
        return filmStorage.getFilms().get(id);
    }

    @Override
    public int compare(Film f0, Film f1) {
        int result = Integer.compare(f1.getLikes().size(), f0.getLikes().size());
        return result;
    }
}
