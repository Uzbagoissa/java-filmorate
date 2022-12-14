package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
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
    private final FilmStorageDB filmStorageDB;
    private final UserValidateDB userValidateDB;
    private final Logger log = LoggerFactory.getLogger(UserStorageDB.class);
    private final JdbcTemplate jdbcTemplate;

    public FilmServiceDB(JdbcTemplate jdbcTemplate, FilmValidateDB filmValidateDB, FilmStorageDB filmStorageDB, UserValidateDB userValidateDB) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidateDB = filmValidateDB;
        this.filmStorageDB = filmStorageDB;
        this.userValidateDB = userValidateDB;
    }
    @Override
    public List<Film> getMostPopularFilms(Integer countPopFilms) {
        String sql = "select count(USER_ID) as COUNT, FILM_ID from FILM_USER_LIKE group by FILM_ID order by COUNT limit ?";
        List<Integer> filmsIdLike = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("film_id"), countPopFilms);
        List<Film> filmsAll = filmStorageDB.getAllFilms();
        List<Film> filmsPopular = new ArrayList<>();
        if (countPopFilms - filmsIdLike.size() > 0 && countPopFilms - filmsIdLike.size() <= filmsAll.size()){
            filmsAll.removeAll(filmsPopular);
            for (int i = 0; i < countPopFilms - filmsIdLike.size(); i++) {
                filmsPopular.add(filmsAll.get(i));
            }
        } else if (countPopFilms - filmsIdLike.size() > 0 && countPopFilms - filmsIdLike.size() > filmsAll.size()){
            filmsAll.removeAll(filmsPopular);
            filmsPopular.addAll(filmsAll);
        } else if (countPopFilms - filmsIdLike.size() <= 0){
            for (Integer integer : filmsIdLike) {
                filmsPopular.add(getFilm(integer));
            }
        }
        log.info("?????????????? ???????????? ?????????? ???????????????????? ??????????????");
        return filmsPopular;
    }

    @Override
    public Film addLike(Integer id, Integer userId) {
        String sql = "select * from FILMS where FILM_ID = ?";                                                            //?????????????????? ????????????
        SqlRowSet filmCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateDB.checkFilmValidate(log, filmCheckRows, id);
        sql= "select * from USERS where USER_ID = ?";                                                                   //?????????????????? ????????????
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, userId);
        userValidateDB.checkUserValidate(log, idCheckRows, userId);
        sql= "select FILM_ID from FILM_USER_LIKE where USER_ID = ? and FILM_ID = ?";                                    //?????????????????? ??????????
        SqlRowSet likeCheckRows = jdbcTemplate.queryForRowSet(sql, userId, id);
        filmValidateDB.checkAddLikeValidate(log, likeCheckRows, userId, id);
        sql = "insert into FILM_USER_LIKE(FILM_ID, USER_ID) values (?, ?)";                                             //??????????
        jdbcTemplate.update(sql,
                id,
                userId);
        log.info("???????????? {} ?????????????????? ???????? ???? ???????????????????????? {}", id, userId);
        return getFilm(id);
    }

    @Override
    public Film removeLike(Integer id, Integer userId) {
        String sql = "select * from FILMS where FILM_ID = ?";                                                            //?????????????????? ????????????
        SqlRowSet filmCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateDB.checkFilmValidate(log, filmCheckRows, id);
        sql= "select * from USERS where USER_ID = ?";                                                                   //?????????????????? ????????????
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, userId);
        userValidateDB.checkUserValidate(log, idCheckRows, userId);
        sql = "delete from FILM_USER_LIKE where FILM_ID = ? and USER_ID = ?";                                           //??????????
        jdbcTemplate.update(sql, id, userId);
        log.info("?? ???????????? {} ???????????? ???????? ???? ???????????????????????? {}", id, userId);
        return getFilm(id);
    }

    private Film getFilm(Integer id) {
        String sql = "select * from FILMS where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        filmRows.next();
        return new Film(
                filmRows.getInt("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date"))),
                filmRows.getInt("duration"),
                filmStorageDB.getMPA(filmRows.getInt("mpa_id")),
                filmRows.getInt("rate"),
                getSingleListGenre(id)
        );
    }

    private List<Genre> getSingleListGenre(Integer filmId) {                                                             //?????????????????? ??????????????, ??.??. ???????????????????????? ?? ???????????????????? ????????????
        String sql = "select GENRE_ID from FILM_GENRE where FILM_ID = ?";
        List<Integer> genresId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("genre_id"), filmId);
        List<Genre> genres = new ArrayList<>();
        for (Integer integer : genresId) {
            genres.add(filmStorageDB.getGenre(integer));
        }
        return genres;
    }

}
