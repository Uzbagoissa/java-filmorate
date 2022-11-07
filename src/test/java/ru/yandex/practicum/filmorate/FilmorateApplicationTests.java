package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmorateApplicationTests {
	private final UserStorageDB userStorageDB;
	@Test
	void contextLoads() {
	}

	@Test
	public void testGetUser() {
		User user1 = new User(1, "email@email.ru", "name", "login", LocalDate.parse("2000-01-12"));
		userStorageDB.createUser(user1);
		Optional<User> userOptional  = Optional.ofNullable(userStorageDB.getUser(1));
		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user ->	assertThat(user).hasFieldOrPropertyWithValue("id", 1));
	}

}
