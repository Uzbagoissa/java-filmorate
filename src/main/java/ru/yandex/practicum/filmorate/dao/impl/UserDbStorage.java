package ru.yandex.practicum.filmorate.dao.impl;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidateServiceDb;
import ru.yandex.practicum.filmorate.service.UserValidateServiceStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserValidateServiceDb userValidateServiceDb;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidateServiceDb userValidateServiceDb) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidateServiceDb = userValidateServiceDb;
    }

    @Override
    public User getUser(Integer id) {
        String sql = "select * from USERR where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateServiceDb.checkUserValidate(log, userRows, id);
        User user = new User(
                userRows.getInt("user_id"),
                userRows.getString("email"),
                userRows.getString("name"),
                userRows.getString("login"),
                LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")))
        );
        log.info("Найден пользователь");
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "select * from USERR";
        log.info("Найдены пользователи");
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sqlCheck = "select * from USERR where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlCheck, user.getId());
        userValidateServiceDb.checkCreateUserValidate(log, userRows, user);
        String sql = "insert into USERR(USER_ID, EMAIL, NAME, LOGIN, BIRTHDAY) values ( ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday());
        log.info("Добавлен новый пользователь, {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlCheck = "select * from USERR where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlCheck, user.getId());
        userValidateServiceDb.checkUserValidate(log, userRows, user.getId());
        userValidateServiceDb.checkCreateUserValidate(log, userRows, user);
        String sql = "update USERR set USER_ID = ?, EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        log.info("Пользователь обновлен - , {}", user);
        return user;
    }

    private User mapRowToUser(ResultSet rs) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .login(rs.getString("login"))
                .birthday(LocalDate.parse(Objects.requireNonNull(rs.getString("birthday"))))
                .build();
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return null;
    }
}
