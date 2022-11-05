package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.dbStorage.FilmStorageDB;
import ru.yandex.practicum.filmorate.validate.dbValidate.FilmValidateDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;
import ru.yandex.practicum.filmorate.validate.dbValidate.UserValidateDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class FilmServiceDB implements FilmService {
    private final FilmValidateDB filmValidateDB;
    private final UserValidateDB userValidateDB;
    private final Logger log = LoggerFactory.getLogger(UserStorageDB.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmServiceDB(JdbcTemplate jdbcTemplate, FilmValidateDB filmValidateDB, UserValidateDB userValidateDB) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidateDB = filmValidateDB;
        this.userValidateDB = userValidateDB;
    }
    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        String sql = "select count(USER_ID) as COUNT, FILM_ID from FILM_USER_LIKE group by FILM_ID order by COUNT DESC limit ?";
        List<Integer> filmsId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("film_id"), count);
        List<Film> films = new ArrayList<>();
        for (int i = 0; i < filmsId.size(); i++) {
            films.add(getFilm(filmsId.get(i)));
        }
        log.info("Получен список самых популярных фильмов");
        return films;
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация фильма
        SqlRowSet filmCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateDB.checkFilmValidate(log, filmCheckRows, id);
        sql= "select * from USERR where USER_ID = ?";                                                                   //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, userId);
        userValidateDB.checkUserValidate(log, idCheckRows, userId);
        sql= "select FILM_ID from FILM_USER_LIKE where USER_ID = ? and FILM_ID = ?";                                    //валидация лайка
        SqlRowSet likeCheckRows = jdbcTemplate.queryForRowSet(sql, userId, id);
        filmValidateDB.checkAddLikeValidate(log, likeCheckRows, userId, id);
        sql = "insert into FILM_USER_LIKE(FILM_ID, USER_ID) values (?, ?)";                                             //метод
        jdbcTemplate.update(sql,
                id,
                userId);
        log.info("Фильму {} поставлен лайк от пользователя {}", id, userId);
        return getFilm(id);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация фильма
        SqlRowSet filmCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateDB.checkFilmValidate(log, filmCheckRows, id);
        sql= "select * from USERR where USER_ID = ?";                                                                   //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, userId);
        userValidateDB.checkUserValidate(log, idCheckRows, userId);
        sql = "delete from FILM_USER_LIKE where FILM_ID = ? and USER_ID = ?";                                           //метод
        jdbcTemplate.update(sql, id, userId);
        log.info("У фильма {} удален лайк от пользователя {}", id, userId);
        return getFilm(id);
    }

    private Film getFilm(Integer id) {
        String sql = "select * from FILM where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        filmRows.next();
        Film film = new Film(
                filmRows.getInt("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date"))),
                filmRows.getInt("duration"),
                filmRows.getInt("rate")
        );
        return film;
    }

}
