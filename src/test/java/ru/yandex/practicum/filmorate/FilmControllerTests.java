package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTests {
    Film film = new Film();
    FilmController filmController = new FilmController();

    @Test
    void getFilmAlreadyExistException() throws ValidationException {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        filmController.addFilm(film);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void getInvalidFilmNameException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName(" ");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void getInvalidDescriptionException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизниблаблаблаблабалбалбалабалабалшзжпоажышвпожвы" +
                "вапавапавадвлшьььоавпвжапжвапжвапжвапждвапждвлпждвлжадплвжадплвжадплжвдаплжвдаплж" +
                "ваьпвдапвдаьпждвапждвапждважпдважпжвапжвдапвапжвапдвапвэапвэапэвапэвапэвдаэпждваэп");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void getInvalidReleaseDateException() {
        film.setDuration(2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(1200, 10, 14));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void getInvalidDurationException() {
        film.setDuration(-2);
        film.setDescription("Бла бла бла про то, как все вечно в этой жизни");
        film.setName("Интерстеллар");
        film.setReleaseDate(LocalDate.of(2011, 10, 14));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void getInvalidFilmException() {
        assertThrows(ValidationException.class, () -> filmController.renewFilm(film));
    }

}
