package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserControllerTests {
    User user = new User();
    UserController userController = new UserController();

    @BeforeEach
    public void beforeEach() {
        user.setName("Вася");
    }

    @Test
    void getUserAlreadyExistException() throws ValidationException {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("VACE");
        user.setEmail("vace@yandex.ru");
        userController.createUser(user);
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void getInvalidEmailException() {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("VACE");
        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void getInvalidLoginException() {
        user.setBirthday(LocalDate.parse("2000-01-12"));
        user.setLogin("  ");
        user.setEmail("vace@yandex.ru");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void getInvalidBirthdayException() {
        user.setBirthday(LocalDate.parse("2200-01-12"));
        user.setLogin("VACE");
        user.setEmail("vace@yandex.ru");
        assertThrows(ValidationException.class, () -> userController.createUser(user));
    }

    @Test
    void getInvalidUserException() {
        assertThrows(ValidationException.class, () -> userController.updateUser(user));
    }

}
