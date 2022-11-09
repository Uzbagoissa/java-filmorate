package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.imStorage.FilmStorageIM;
import ru.yandex.practicum.filmorate.validate.imValidate.FilmValidateIM;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmControllerTests {
    private final Film film = new Film();
    private final JdbcTemplate jdbcTemplate;
    private final FilmValidateIM filmValidateIM = new FilmValidateIM();
    private final FilmStorageIM filmStorageIM = new FilmStorageIM(filmValidateIM);

    @Test
    void getFilmAlreadyExistException() throws ValidationException {
        List<Genre> genres = new ArrayList<>();
        Mpa mpa = new Mpa(1, "G");
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        film.setGenres(genres);
        film.setRate(3);
        film.setMpa(mpa);
        filmStorageIM.addFilm(film);
        assertThrows(ValidationException.class, () -> filmStorageIM.addFilm(film));
    }

    @Test
    void getInvalidFilmNameException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorageIM.addFilm(film));
    }

    @Test
    void getInvalidDescriptionException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизниблаблаблаблабалбалбалабалабалшзжпоажышвпожвы" +
                "вапавапавадвлшьььоавпвжапжвапжвапжвапждвапждвлпждвлжадплвжадплвжадплжвдаплжвдаплж" +
                "ваьпвдапвдаьпждвапждвапждважпдважпжвапжвдапвапжвапдвапвэапвэапэвапэвапэвдаэпждваэп");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorageIM.addFilm(film));
    }

    @Test
    void getInvalidReleaseDateException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(1200, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorageIM.addFilm(film));
    }

    @Test
    void getInvalidDurationException() {
        film.setDuration(-2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorageIM.addFilm(film));
    }

    @Test
    void getInvalidFilmException() {
        assertThrows(NotFoundException.class, () -> filmStorageIM.updateFilm(film));
    }

    @AfterEach                                                                                                          //чистим таблицы для проверок постманом
    void tearDown() {
        jdbcTemplate.update("DELETE FROM FILM_USER_LIKE");
        jdbcTemplate.update("DELETE FROM FILM_GENRE");
        jdbcTemplate.update("DELETE FROM FRIEND_STATUS");
        jdbcTemplate.update("DELETE FROM USERS");
        jdbcTemplate.update("DELETE FROM FILMS");
        jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILM_GENRE ALTER COLUMN FILM_GENRE_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FILM_USER_LIKE ALTER COLUMN LIKE_ID RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE FRIEND_STATUS ALTER COLUMN FRIEND_STATUS_ID RESTART WITH 1");
    }
}
