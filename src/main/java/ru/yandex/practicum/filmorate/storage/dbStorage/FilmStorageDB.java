package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validate.dbValidate.FilmValidateDB;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
                getSingleMPA(filmRows.getInt("mpa_id")),
                filmRows.getInt("rate"),
                getSingleListGenre(id)
        );
        log.info("Найден фильм");
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Найдены фильмы");
        return getListAllFilms();
    }

    @Override
    public Film addFilm(Film film) {
        String sql = "select * from FILM where FILM_ID = ?";                                                            //валидация создания фильма
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getId());
        filmValidateDB.checkAddFilmValidate(log, filmRows, film);
        String sqlq = "insert into FILM(NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID, RATE) values ( ?, ?, ?, ?, ?, ? )";  //метод
        KeyHolder keyHolder = new GeneratedKeyHolder();                                                                 //задали айдишник фильму
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlq, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            ps.setInt(6, film.getRate());
            return ps;
        },keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        log.info("Добавлен новый фильм {}", film);
        return addGenre(film);
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
        log.info("Фильм обновлен - {}", film);
        return addGenre(film);
    }

    @Override
    public List<Genre> getGenres() {
        String sql = "select * from GENRE";
        log.info("Найдены жанры");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenre(rs));
    }

    @Override
    public Genre getGenre(Integer genreId) {
        log.info("Найден жанр");
        return getSingleGenre(genreId);
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

    public Mpa getSingleMPA(Integer mpaId) {                                                                            //отдельным методом, т.к. задействован в нескольких местах
        String sql = "select * from MPA where MPA_ID = ?";                                                              //валидация рейтинга
        SqlRowSet ratingMPARows = jdbcTemplate.queryForRowSet(sql, mpaId);
        filmValidateDB.checkRatingMPAValidate(log, ratingMPARows, mpaId);
        Mpa mpa = new Mpa(                                                                                              //метод
                ratingMPARows.getInt("mpa_id"),
                ratingMPARows.getString("name")
        );
        return mpa;
    }

    public Genre getSingleGenre(Integer genreId) {                                                                      //отдельным методом, т.к. задействован в нескольких местах
        String sql = "select * from GENRE where GENRE_ID = ?";                                                          //валидация жанра
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sql, genreId);
        filmValidateDB.checkGenreValidate(log, genreRows, genreId);
        Genre genre = new Genre(                                                                                        //метод
                genreRows.getInt("genre_id"),
                genreRows.getString("name")
        );
        return genre;
    }

    public List<Genre> getSingleListGenre(Integer filmId) {                                                             //отдельным методом, т.к. задействован в нескольких местах
        String sql = "select GENRE_ID from FILM_GENRE where FILM_ID = ?";
        List<Integer> genresId = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToGenreId(rs), filmId);
        List<Genre> genres = new ArrayList<>();
        for (int i = 0; i < genresId.size(); i++) {
            genres.add(getSingleGenre(genresId.get(i)));
        }
        return genres;
    }

    public List<Film> getListAllFilms() {
        String sql = "select * from FILM";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToFilm(rs));
        for (int i = 0; i < films.size(); i++) {
            films.get(i).setGenres(getSingleListGenre(films.get(i).getId()));
        }
        return films;
    }

    private Film addGenre(Film film) {
        film.setGenres(film.getGenres().stream().distinct().collect(Collectors.toList()));                              //удаление дубликатов жанров
        for (int i = 0; i < film.getGenres().stream().distinct().count(); i++) {
            String sql = "insert into FILM_GENRE(FILM_ID, GENRE_ID) values (?, ?)";
            jdbcTemplate.update(sql,
                    film.getId(),
                    film.getGenres().get(i).getId());
        }
        return film;
    }

    public Film mapRowToFilm(ResultSet rs) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(LocalDate.parse(Objects.requireNonNull(rs.getString("release_date"))))
                .duration(rs.getInt("duration"))
                .mpa(getSingleMPA(rs.getInt("mpa_id")))
                .rate(rs.getInt("rate"))
                .build();
    }

    private Genre mapRowToGenre(ResultSet rs) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }

    private Integer mapRowToGenreId(ResultSet rs) throws SQLException {
        return rs.getInt("genre_id");
    }

    private Mpa mapRowToRatingMPA(ResultSet rs) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return null;
    }
}
