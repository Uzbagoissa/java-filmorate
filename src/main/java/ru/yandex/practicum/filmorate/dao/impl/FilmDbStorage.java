package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmValidateServiceDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmValidateServiceDb filmValidateServiceDb;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidateServiceDb filmValidateServiceDb) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidateServiceDb = filmValidateServiceDb;
    }

    @Override
    public Film getFilm(Integer id) {
        String sql = "select * from FILM where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateServiceDb.checkFilmValidate(log, filmRows, id);
        Film film = new Film(
                filmRows.getInt("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date"))),
                filmRows.getInt("duration"),
                filmRows.getInt("rating_id")
        );
        log.info("Найден фильм");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));
    }

    @Override
    public Film addFilm(Film film) {
        String sqlCheck = "select * from FILM where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlCheck, film.getId());
        filmValidateServiceDb.checkAddFilmValidate(log, filmRows, film);
        String sqlFilm = "insert into FILM(FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) values ( ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlFilm,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating());
        for (int i = 0; i < film.getGenres().size(); i++) {
            String sqlFilmGenre = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sqlFilmGenre,
                    film.getId(),
                    film.getGenres().get(i));
        }
        log.info("Добавлен новый фильм, {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlCheck = "select * from FILM where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlCheck, film.getId());
        filmValidateServiceDb.checkFilmValidate(log, filmRows, film.getId());
        filmValidateServiceDb.checkAddFilmValidate(log, filmRows, film);
        String sql = "update FILM set FILM_ID = ?, NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATING_ID = ? where FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating(),
                film.getId());
        log.info("Фильм обновлен - , {}", film);
        return film;
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(LocalDate.parse(Objects.requireNonNull(rs.getString("release_date"))))
                .duration(rs.getInt("duration"))
                .rating(rs.getInt("rating_id"))
                .build();
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return null;
    }
}
