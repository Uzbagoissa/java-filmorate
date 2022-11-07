package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
class FilmControllerTests {
    Film film = new Film();
    FilmValidateIM filmValidateIM = new FilmValidateIM();
    FilmStorageIM filmStorage = new FilmStorageIM(filmValidateIM);

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
        filmStorage.addFilm(film);
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    void getInvalidFilmNameException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
        //
    }

    @Test
    void getInvalidDescriptionException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизниблаблаблаблабалбалбалабалабалшзжпоажышвпожвы" +
                "вапавапавадвлшьььоавпвжапжвапжвапжвапждвапждвлпждвлжадплвжадплвжадплжвдаплжвдаплж" +
                "ваьпвдапвдаьпждвапждвапждважпдважпжвапжвдапвапжвапдвапвэапвэапэвапэвапэвдаэпждваэп");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    void getInvalidReleaseDateException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(1200, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    void getInvalidDurationException() {
        film.setDuration(-2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmStorage.addFilm(film));
    }

    @Test
    void getInvalidFilmException() {
        assertThrows(NotFoundException.class, () -> filmStorage.updateFilm(film));
    }

}
