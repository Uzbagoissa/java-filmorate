package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void getUsers() throws UserAlreadyExistException, InvalidLoginException, InvalidBirthdayException, InvalidEmailException {
		User user = new User();
		user.setName("Вася");
		user.setBirthday(LocalDate.parse("2000-01-12"));
		user.setLogin("VACE");
		user.setEmail("vace@yandex.ru");
		UserController userController = new UserController();
		userController.createUser(user);
		assertEquals("Такой пользователь уже существует!", userController.createUser(user));
	}

}
