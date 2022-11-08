package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.dbService.UserServiceDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmorateApplicationTests {
	private final UserStorageDB userStorageDB;
	private final UserServiceDB userServiceDB;
	@Test
	void contextLoads() {
	}

	@Test
	void testCreateUser() {
		User user1 = new User();
		user1.setName("name");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name");
		user1.setEmail("ssf@yandex.ru");
		assertEquals(userStorageDB.createUser(user1), user1);
	}

	@Test
	void testGetUser() {
		User user1 = new User();
		user1.setName("name");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name");
		user1.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		assertEquals(userStorageDB.getUser(1), user1);
	}

	@Test
	void testUpdateUser() {
		User user1 = new User();
		user1.setName("name");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name");
		user1.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		user1.setLogin("updatename");
		userStorageDB.updateUser(user1);
		assertEquals(userStorageDB.getUser(1).getLogin(), "updatename");
	}

	@Test
	void testGetAllUsers() {
		User user1 = new User();
		user1.setName("name1");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name1");
		user1.setEmail("ssf@yandex.ru");
		User user2 = new User();
		user2.setName("name2");
		user2.setBirthday(LocalDate.parse("2000-01-12"));
		user2.setLogin("name2");
		user2.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		assertEquals(userStorageDB.getAllUsers(), List.of(user1, user2));
	}

	@Test
	void testAddFriend() {
		User user1 = new User();
		user1.setName("name1");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name1");
		user1.setEmail("ssf@yandex.ru");
		User user2 = new User();
		user2.setName("name2");
		user2.setBirthday(LocalDate.parse("2000-01-12"));
		user2.setLogin("name2");
		user2.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of(user2));
	}

	@Test
	void testRemoveFriend() {
		User user1 = new User();
		user1.setName("name1");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name1");
		user1.setEmail("ssf@yandex.ru");
		User user2 = new User();
		user2.setName("name2");
		user2.setBirthday(LocalDate.parse("2000-01-12"));
		user2.setLogin("name2");
		user2.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		userServiceDB.removeFriend(user1.getId(), user2.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of());
	}

	@Test
	void testGetAllFriends() {
		User user1 = new User();
		user1.setName("name1");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name1");
		user1.setEmail("ssf@yandex.ru");
		User user2 = new User();
		user2.setName("name2");
		user2.setBirthday(LocalDate.parse("2000-01-12"));
		user2.setLogin("name2");
		user2.setEmail("ssf@yandex.ru");
		User user3 = new User();
		user3.setName("name3");
		user3.setBirthday(LocalDate.parse("2000-01-12"));
		user3.setLogin("name3");
		user3.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of(user2));
		userServiceDB.addFriend(user1.getId(), user3.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of(user2, user3));
	}

	@Test
	void testGetCommonFriends() {
		User user1 = new User();
		user1.setName("name1");
		user1.setBirthday(LocalDate.parse("2000-01-12"));
		user1.setLogin("name1");
		user1.setEmail("ssf@yandex.ru");
		User user2 = new User();
		user2.setName("name2");
		user2.setBirthday(LocalDate.parse("2000-01-12"));
		user2.setLogin("name2");
		user2.setEmail("ssf@yandex.ru");
		User user3 = new User();
		user3.setName("name3");
		user3.setBirthday(LocalDate.parse("2000-01-12"));
		user3.setLogin("name3");
		user3.setEmail("ssf@yandex.ru");
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		userServiceDB.addFriend(user3.getId(), user2.getId());
		assertEquals(userServiceDB.getCommonFriends(user1.getId(), user3.getId()), List.of(user2));
	}

}
