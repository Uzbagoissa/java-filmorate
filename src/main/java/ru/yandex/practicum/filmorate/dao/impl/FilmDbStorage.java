package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmValidateServiceDb;

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
                filmRows.getInt("rate")
        );
        film.setMpa(getSingleMPA(id));
        log.info("Найден фильм");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM";
        log.info("Найдены фильмы");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));
    }

    @Override
    public Film addFilm(Film film) {
        String sqlCheck = "select * from FILM where FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlCheck, film.getId());
        filmValidateServiceDb.checkAddFilmValidate(log, filmRows, film);
        String sqlFilm = "insert into FILM(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, RATE) values ( ?, ?, ?, ?, ?, ? )";
        jdbcTemplate.update(sqlFilm,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate());
        String sqlID = "select FILM_ID from FILM where NAME = ?";
        SqlRowSet filmRowsID = jdbcTemplate.queryForRowSet(sqlID, film.getName());
        filmRowsID.next();
        film.setId(filmRowsID.getInt("film_id"));
        film.setMpa(getSingleMPA(film.getId()));
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
        String sql = "update FILM set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?, RATE = ? where FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        String sqlFilmGenreDel = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlFilmGenreDel, film.getId());
        for (int i = 0; i < film.getGenres().size(); i++) {
            String sqlFilmGenre = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sqlFilmGenre,
                    film.getId(),
                    film.getGenres().get(i));
        }
        log.info("Фильм обновлен - , {}", film);
        return film;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from GENRE";
        log.info("Найдены жанры");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getGenre(Integer id) {
        String sql = "select * from GENRE where GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateServiceDb.checkGenreValidate(log, genreRows, id);
        Genre genre = new Genre(
                genreRows.getInt("genre_id"),
                genreRows.getString("name")
        );
        log.info("Найден жанр");
        return genre;
    }

    @Override
    public Mpa getMPA(Integer id) {
        log.info("Найден рейтинг");
        return getSingleMPA(id);
    }

    @Override
    public List<Mpa> getMPAs() {
        String sql = "select * from MPA";
        log.info("Найдены рейтинги");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToRatingMPA(rs));
    }

    private Film mapRowToFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(LocalDate.parse(Objects.requireNonNull(rs.getString("release_date"))))
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    private Mpa mapRowToRatingMPA(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }

    public Mpa getSingleMPA(Integer id) {
        String sql = "select * from MPA where MPA_ID = ?";
        SqlRowSet ratingMPARows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateServiceDb.checkRatingMPAValidate(log, ratingMPARows, id);
        Mpa mpa = new Mpa(
                ratingMPARows.getInt("mpa_id"),
                ratingMPARows.getString("name")
        );
        return mpa;
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return null;
    }
}
