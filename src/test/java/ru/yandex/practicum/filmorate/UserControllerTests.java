package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.imStorage.UserStorageIM;
import ru.yandex.practicum.filmorate.validate.imValidate.UserValidateIM;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTests {
    User user = new User();
    UserValidateIM userValidateIM = new UserValidateIM();
    UserStorageIM userStorageIM = new UserStorageIM(userValidateIM);

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

}
