package ru.yandex.practicum.filmorate.dao.implService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserService;
import ru.yandex.practicum.filmorate.dao.implStorage.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Status;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidateServiceDb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserDbService implements UserService {
    private final UserValidateServiceDb userValidateServiceDb;
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDbService(JdbcTemplate jdbcTemplate, UserValidateServiceDb userValidateServiceDb) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidateServiceDb = userValidateServiceDb;
    }
    @Override
    public List<User> getAllFriends(Integer id) {
        String sql = "select FRIEND_ID from FRIEND_STATUS where USER_ID = ?";
        List<Integer> friendsId = jdbcTemplate.query(sql, (rs, rowNum) -> getFriendId(rs), id);
        List<User> friends = new ArrayList<>();
        for (int i = 0; i < friendsId.size(); i++) {
            sql = "select * from USERR where USER_ID = ?";
            SqlRowSet friendRows = jdbcTemplate.queryForRowSet(sql, friendsId.get(i));
            friendRows.next();
            User user = new User(
                    friendRows.getInt("user_id"),
                    friendRows.getString("email"),
                    friendRows.getString("name"),
                    friendRows.getString("login"),
                    LocalDate.parse(Objects.requireNonNull(friendRows.getString("birthday")))
            );
            friends.add(user);
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return null;
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        /*String sql= "select * from USERR where USER_ID = ?";                                                          //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateServiceDb.checkUserValidate(log, idCheckRows, id);
        userValidateServiceDb.checkUserValidate(log, idCheckRows, friendId);*/
        /*String sql = "select count(FRIEND_ID) as COUNT from FRIEND_STATUS where USER_ID = ?";                         //валидация дружбы
        SqlRowSet checkRows = jdbcTemplate.queryForRowSet(sql, id);
        checkRows.next();
        for (int i = 0; i < checkRows.getInt("count"); i++) {
            sql = "select FRIEND_ID from FRIEND_STATUS where USER_ID = ?";
            SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
            userValidateServiceDb.checkAddFriendValidate(log, idCheckRows, friendId);
        }*/
        String sql = "insert into FRIEND_STATUS(USER_ID, FRIEND_ID, STATUS) values (?, ?, FALSE)";
        jdbcTemplate.update(sql,
                friendId,
                id);
        sql = "select * from USERR where USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, friendId);
        userRows.next();
        User user = new User(
                userRows.getInt("user_id"),
                userRows.getString("email"),
                userRows.getString("name"),
                userRows.getString("login"),
                LocalDate.parse(Objects.requireNonNull(userRows.getString("birthday")))
        );
        log.info("Пользователь, {} теперь в друзьях пользователя, {}", id, friendId);
        return user;
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        return null;
    }

    private Integer getFriendId(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }
}
