package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.imStorage.UserStorageIM;
import ru.yandex.practicum.filmorate.validate.imValidate.UserValidateIM;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserControllerTests {
    private final User user = new User();
    private final JdbcTemplate jdbcTemplate;
    private final UserValidateIM userValidateIM = new UserValidateIM();
    private final UserStorageIM userStorageIM = new UserStorageIM(userValidateIM);

    @BeforeEach
    public void beforeEach() {
        user.setName("Вася");
    }

    @Test
    void getUserAlreadyExistException() throws ValidationException {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("VACE");
        user.setEmail("vace@yandex.ru");
        userStorageIM.createUser(user);
        assertThrows(ValidationException.class, () -> userStorageIM.createUser(user));
    }

    @Test
    void getInvalidEmailException() {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("VACE");
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userStorageIM.createUser(user));
    }

    @Test
    void getInvalidLoginException() {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("  ");
        user.setEmail("vace@yandex.ru");
        assertThrows(ValidationException.class, () -> userStorageIM.createUser(user));
    }

    @Test
    void getInvalidBirthdayException() {
        user.setBirthday(LocalDate.parse("2200-01-12"));
        user.setLogin("VACE");
        user.setEmail("vace@yandex.ru");
        assertThrows(ValidationException.class, () -> userStorageIM.createUser(user));
    }

    @Test
    void getInvalidUserException() {
        assertThrows(NotFoundException.class, () -> userStorageIM.updateUser(user));
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
