package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validate.dbValidate.FilmValidateDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class FilmStorageDB implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmStorageDB.class);
    private final JdbcTemplate jdbcTemplate;
    private final FilmValidateDB filmValidateDB;

    public FilmStorageDB(JdbcTemplate jdbcTemplate, FilmValidateDB filmValidateDB) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmValidateDB = filmValidateDB;
    }

    @Override
    public Film getFilm(Integer id) {
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация фильма
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);
        filmValidateDB.checkFilmValidate(log, filmRows, id);
        Film film = new Film(                                                                                           //метод
                filmRows.getInt("film_id"),
                filmRows.getString("name"),
                filmRows.getString("description"),
                LocalDate.parse(Objects.requireNonNull(filmRows.getString("release_date"))),
                filmRows.getInt("duration"),
                filmRows.getInt("rate")
        );
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
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация создания фильма
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getId());
        filmValidateDB.checkAddFilmValidate(log, filmRows, film);
        sql = "insert into FILM(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, RATE) values ( ?, ?, ?, ?, ?, ? )";  //метод
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate());
        sql = "select FILM_ID from FILM where NAME = ?";
        SqlRowSet filmRowsID = jdbcTemplate.queryForRowSet(sql, film.getName());
        filmRowsID.next();
        film.setId(filmRowsID.getInt("film_id"));                                                             //задали айдишник фильму
        for (int i = 0; i < film.getGenres().size(); i++) {
            sql = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sql,
                    film.getId(),
                    film.getGenres().get(i).getId());
        }
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация фильма
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getId());
        filmValidateDB.checkFilmValidate(log, filmRows, film.getId());
        filmValidateDB.checkAddFilmValidate(log, filmRows, film);                                                       //валидация создания фильма
        sql = "update FILM set NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ?, RATE = ? " +       //метод
                "where FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        sql = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
        for (int i = 0; i < film.getGenres().size(); i++) {
            sql = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sql,
                    film.getId(),
                    film.getGenres().get(i).getId());
        }
        log.info("Фильм обновлен - {}", film);
        return film;
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from GENRE";
        log.info("Найдены жанры");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getGenre(Integer genreId) {
        String sql = "select * from GENRE where GENRE_ID = ?";                                                          //валидация жанра
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, genreId);
        filmValidateDB.checkGenreValidate(log, genreRows, genreId);
        Genre genre = new Genre(                                                                                        //метод
                genreRows.getInt("genre_id"),
                genreRows.getString("name")
        );
        log.info("Найден жанр");
        return genre;
    }

    @Override
    public Mpa getMPA(Integer mpaId) {
        log.info("Найден рейтинг");
        return getSingleMPA(mpaId);
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

    private Mpa getSingleMPA(Integer mpaId) {                                                                            //отдельным методом, т.к. задействован в нескольких местах
        String sql = "select * from MPA where MPA_ID = ?";                                                              //валидация рейтинга
        SqlRowSet ratingMPARows = jdbcTemplate.queryForRowSet(sql, mpaId);
        filmValidateDB.checkRatingMPAValidate(log, ratingMPARows, mpaId);
        Mpa mpa = new Mpa(                                                                                              //метод
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
