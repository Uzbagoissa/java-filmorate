package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.dbService.FilmServiceDB;
import ru.yandex.practicum.filmorate.service.dbService.UserServiceDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.FilmStorageDB;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmControllerDbTests {
	private final UserStorageDB userStorageDB;
	private final UserServiceDB userServiceDB;
	private final FilmStorageDB filmStorageDB;
	private final FilmServiceDB filmServiceDB;
	private final JdbcTemplate jdbcTemplate;
	User user1 = new User(1, "ssf@yandex.ru", "name1", "login1", LocalDate.parse("1987-01-12"));
	User user2 = new User(2, "ssf@yandex.ru", "name2", "login2", LocalDate.parse("1987-01-12"));
	User user3 = new User(3, "ssf@yandex.ru", "name3", "login3", LocalDate.parse("1987-01-12"));
	Mpa mpa1 = new Mpa(1, "G");
	Mpa mpa2 = new Mpa(2, "PG");
	Mpa mpa3 = new Mpa(3, "PG-13");
	Mpa mpa4 = new Mpa(4, "R");
	Mpa mpa5 = new Mpa(5, "NC-17");
	Genre genre1 = new Genre(1, "Комедия");
	Genre genre2 = new Genre(2, "Драма");
	Genre genre3 = new Genre(3, "Мультфильм");
	Genre genre4 = new Genre(4, "Триллер");
	Genre genre5 = new Genre(5, "Документальный");
	Genre genre6 = new Genre(6, "Боевик");
	List<Genre> genreList1 = Arrays.asList(genre1, genre2);
	List<Genre> genreList2 = Arrays.asList(genre3);
	List<Genre> genreList3 = Arrays.asList(genre2, genre3);
	Film film1 = new Film(1, "name1", "description1", LocalDate.parse("1987-01-12"), 2, mpa1, 4, genreList1);
	Film film2 = new Film(2, "name2", "description2", LocalDate.parse("1987-01-12"), 1, mpa2, 4, genreList2);
	Film film3 = new Film(3, "name3", "description3", LocalDate.parse("1987-01-12"), 3, mpa3, 4, genreList3);
	@Test
	void testAddFilm() {
		assertEquals(filmStorageDB.addFilm(film1), film1);
	}

	@Test
	void testUpdateFilm() {
		filmStorageDB.addFilm(film1);
		film1.setDescription("newdescription");
		filmStorageDB.updateFilm(film1);
		assertEquals(filmStorageDB.getFilm(film1.getId()).getDescription(), "newdescription");
	}

	@Test
	void testGetAllFilms() {
		filmStorageDB.addFilm(film1);
		filmStorageDB.addFilm(film2);
		assertEquals(filmStorageDB.getAllFilms(), List.of(film1, film2));
	}

	@Test
	void testGetFilm() {
		filmStorageDB.addFilm(film1);
		assertEquals(filmStorageDB.getFilm(film1.getId()), film1);
	}

	@Test
	void testAddLike() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		filmStorageDB.addFilm(film1);
		filmServiceDB.addLike(film1.getId(), user1.getId());
		filmServiceDB.addLike(film1.getId(), user2.getId());
		filmServiceDB.addLike(film1.getId(), user3.getId());
		String sql = "select USER_ID from FILM_USER_LIKE where FILM_ID = ?";                                                            //валидация фильма
		List<Integer> usersAddLikeId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), film1.getId());
		assertEquals(usersAddLikeId, List.of(user1.getId(), user2.getId(), user3.getId()));
	}

	@Test
	void testRemoveLike() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		filmStorageDB.addFilm(film1);
		filmServiceDB.addLike(film1.getId(), user1.getId());
		filmServiceDB.addLike(film1.getId(), user2.getId());
		filmServiceDB.addLike(film1.getId(), user3.getId());
		filmServiceDB.removeLike(film1.getId(), user3.getId());
		String sql = "select USER_ID from FILM_USER_LIKE where FILM_ID = ?";                                                            //валидация фильма
		List<Integer> usersAddLikeId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt("user_id"), film1.getId());
		assertEquals(usersAddLikeId, List.of(user1.getId(), user2.getId()));
	}

	@Test
	void testGetMostPopularFilms() {
		userStorageDB.createUser(user1);
		userStorageDB.createUser(user2);
		userStorageDB.createUser(user3);
		filmStorageDB.addFilm(film1);
		filmStorageDB.addFilm(film2);
		filmStorageDB.addFilm(film3);
		filmServiceDB.addLike(film1.getId(), user1.getId());
		filmServiceDB.addLike(film2.getId(), user2.getId());
		filmServiceDB.addLike(film2.getId(), user3.getId());
		assertEquals(filmServiceDB.getMostPopularFilms(5), List.of(film1, film2, film3));
	}

	@Test
	void testGetMPA() {
		assertEquals(filmStorageDB.getMPA(1), mpa1);
		assertEquals(filmStorageDB.getMPA(2), mpa2);
		assertEquals(filmStorageDB.getMPA(3), mpa3);
		assertEquals(filmStorageDB.getMPA(4), mpa4);
		assertEquals(filmStorageDB.getMPA(5), mpa5);
	}

	@Test
	void testGetMPAs() {
		assertEquals(filmStorageDB.getMPAs(), List.of(mpa1, mpa2, mpa3, mpa4, mpa5));
	}

	@Test
	void testGetGenres() {
		assertEquals(filmStorageDB.getGenres(), List.of(genre1, genre2, genre3, genre4, genre5, genre6));
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
