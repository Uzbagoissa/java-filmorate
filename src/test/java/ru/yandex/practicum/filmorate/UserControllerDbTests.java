package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.dbService.FilmServiceDB;
import ru.yandex.practicum.filmorate.service.dbService.UserServiceDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.FilmStorageDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class UserControllerDbTests {
	private final UserStorageDB userStorageDB;
	private final UserServiceDB userServiceDB;
	private final JdbcTemplate jdbcTemplate;
	User user1 = new User(1, "ssf@yandex.ru", "name1", "login1", LocalDate.parse("1987-01-12"));
	User user2 = new User(2, "ssf@yandex.ru", "name2", "login2", LocalDate.parse("1987-01-12"));
	User user3 = new User(3, "ssf@yandex.ru", "name3", "login3", LocalDate.parse("1987-01-12"));

	@Test
	void testCreateUser() {
		assertEquals(userStorageDB.createUser(user1), user1);
	}

	@Test
	void testGetUser() {
		userStorageDB.createUser(user1);
		assertEquals(userStorageDB.getUser(1), user1);
	}

	@Test
	void testUpdateUser() {
		userStorageDB.createUser(user1);
		user1.setLogin("updatename");
		userStorageDB.updateUser(user1);
		assertEquals(userStorageDB.getUser(user1.getId()).getLogin(), "updatename");
	}

	@Test
	void testGetAllUsers() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		assertEquals(userStorageDB.getAllUsers(), List.of(user1, user2));
	}

	@Test
	void testAddFriend() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of(user2));
	}

	@Test
	void testRemoveFriend() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		userServiceDB.removeFriend(user1.getId(), user2.getId());
		assertEquals(userServiceDB.getAllFriends(user1.getId()), List.of());
	}

	@Test
	void testGetAllFriends() {
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
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		userServiceDB.addFriend(user1.getId(), user2.getId());
		userServiceDB.addFriend(user3.getId(), user2.getId());
		assertEquals(userServiceDB.getCommonFriends(user1.getId(), user3.getId()), List.of(user2));
	}

	@AfterEach
	void tearDown() {
		jdbcTemplate.update("DELETE FROM FILM_USER_LIKE");
		jdbcTemplate.update("DELETE FROM FILM_GENRE");
		jdbcTemplate.update("DELETE FROM FRIEND_STATUS");
		jdbcTemplate.update("DELETE FROM USERS");
		jdbcTemplate.update("DELETE FROM FILM");
		jdbcTemplate.update("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1");
		jdbcTemplate.update("ALTER TABLE FILM ALTER COLUMN FILM_ID RESTART WITH 1");
		jdbcTemplate.update("ALTER TABLE FILM_GENRE ALTER COLUMN FILM_GENRE_ID RESTART WITH 1");
		jdbcTemplate.update("ALTER TABLE FILM_USER_LIKE ALTER COLUMN LIKE_ID RESTART WITH 1");
		jdbcTemplate.update("ALTER TABLE FRIEND_STATUS ALTER COLUMN FRIEND_STATUS_ID RESTART WITH 1");
	}

}
