package ru.yandex.practicum.filmorate.storage.dbStorage;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.dbValidate.UserValidateDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class UserStorageDB implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserStorageDB.class);
    private final JdbcTemplate jdbcTemplate;
    private final UserValidateDB userValidateDB;

    public UserStorageDB(JdbcTemplate jdbcTemplate, UserValidateDB userValidateDB) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidateDB = userValidateDB;
    }

    @Override
    public User getUser(Integer id) {
        String sql = "select * from USERR where USER_ID = ?";                                                           //валидация юзеров
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateDB.checkUserValidate(log, userRows, id);
        User user = new User(                                                                                           //метод
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
        String sql = "select * from USERR where USER_ID = ?";                                                           //валидация создания юзеров
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, user.getId());
        userValidateDB.checkCreateUserValidate(log, userRows, user);
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        sql = "insert into USERR(EMAIL, NAME, LOGIN, BIRTHDAY) values (?, ?, ?, ?)";                                    //метод
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday());
        sql = "select USER_ID from USERR where EMAIL = ?";
        SqlRowSet userRowsID = jdbcTemplate.queryForRowSet(sql, user.getEmail());
        userRowsID.next();
        user.setId(userRowsID.getInt("user_id"));
        log.info("Добавлен новый пользователь {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sql= "select * from USERR where USER_ID = ?";                                                            //валидация юзеров
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, user.getId());
        userValidateDB.checkUserValidate(log, userRows, user.getId());
        userValidateDB.checkCreateUserValidate(log, userRows, user);                                                    //валидация создания юзеров
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        sql = "update USERR set EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ? where USER_ID = ?";                        //метод
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getName(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        log.info("Пользователь обновлен - {}", user);
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
