package ru.yandex.practicum.filmorate.service.dbService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.UserService;
import ru.yandex.practicum.filmorate.storage.dbStorage.UserStorageDB;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.dbValidate.UserValidateDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class UserServiceDB implements UserService {
    private final UserValidateDB userValidateDB;
    private final Logger log = LoggerFactory.getLogger(UserStorageDB.class);
    private final JdbcTemplate jdbcTemplate;

    public UserServiceDB(JdbcTemplate jdbcTemplate, UserValidateDB userValidateDB) {
        this.jdbcTemplate = jdbcTemplate;
        this.userValidateDB = userValidateDB;
    }
    @Override
    public List<User> getAllFriends(Integer id) {
        String sql= "select * from USERS where USER_ID = ?";                                                            //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateDB.checkUserValidate(log, idCheckRows, id);
        sql = "select FRIEND_ID from FRIEND_STATUS where USER_ID = ?";                                                  //метод
        List<Integer> friendsId = jdbcTemplate.query(sql, (rs, rowNum) -> getFriendId(rs), id);
        List<User> friends = new ArrayList<>();
        for (int i = 0; i < friendsId.size(); i++) {
            friends.add(getUser(friendsId.get(i)));
        }
        log.info("Получен список друзей пользователя {}", id);
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sql= "select * from USERS where USER_ID = ?";                                                            //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateDB.checkUserValidate(log, idCheckRows, id);
        SqlRowSet friendIdCheckRows = jdbcTemplate.queryForRowSet(sql, otherId);
        userValidateDB.checkUserValidate(log, friendIdCheckRows, otherId);
        sql = "select FRIEND_ID from FRIEND_STATUS where USER_ID = ?";                                                  //метод
        List<Integer> friendsId = jdbcTemplate.query(sql, (rs, rowNum) -> getFriendId(rs), id);
        List<Integer> friendsOtherId = jdbcTemplate.query(sql, (rs, rowNum) -> getFriendId(rs), otherId);
        List<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < friendsId.size(); i++) {
            if (friendsOtherId.contains(friendsId.get(i))){
                commonFriends.add(getUser(friendsId.get(i)));
            }
        }
        log.info("Получен список общих друзей пользователей {} и {}", id, otherId);
        return commonFriends;
    }

    @Override
    public User addFriend(Integer friendId, Integer id) {
        String sql= "select * from USERS where USER_ID = ?";                                                            //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateDB.checkUserValidate(log, idCheckRows, id);
        SqlRowSet friendIdCheckRows = jdbcTemplate.queryForRowSet(sql, friendId);
        userValidateDB.checkUserValidate(log, friendIdCheckRows, friendId);
        sql= "select USER_ID from FRIEND_STATUS where USER_ID = ? and FRIEND_ID = ?";                                   //валидация дружбы
        SqlRowSet friendshipCheckRows = jdbcTemplate.queryForRowSet(sql, id, friendId);
        userValidateDB.checkAddFriendValidate(log, friendshipCheckRows, id, friendId);
        sql = "insert into FRIEND_STATUS(USER_ID, FRIEND_ID, STATUS) values (?, ?, FALSE)";                             //метод
        jdbcTemplate.update(sql,
                friendId,
                id);
        log.info("Пользователь {} теперь в друзьях пользователя {}", friendId, id);
        return getUser(friendId);
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        String sql= "select * from USERS where USER_ID = ?";                                                            //валидация юзеров
        SqlRowSet idCheckRows = jdbcTemplate.queryForRowSet(sql, id);
        userValidateDB.checkUserValidate(log, idCheckRows, id);
        SqlRowSet friendIdCheckRows = jdbcTemplate.queryForRowSet(sql, friendId);
        userValidateDB.checkUserValidate(log, friendIdCheckRows, friendId);
        sql = "delete from FRIEND_STATUS where USER_ID = ? and FRIEND_ID = ?";                                          //метод
        jdbcTemplate.update(sql, id, friendId);
        log.info("Пользователя {} нет в друзьях пользователя {}", friendId, id);
        return getUser(id);
    }

    private Integer getFriendId(ResultSet rs) throws SQLException {
        return rs.getInt("friend_id");
    }

    private User getUser(Integer id) {
        String sql = "select * from USERS where USER_ID = ?";
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet(sql, id);
        friendRows.next();
        User user = new User(
                friendRows.getInt("user_id"),
                friendRows.getString("email"),
                friendRows.getString("name"),
                friendRows.getString("login"),
                LocalDate.parse(Objects.requireNonNull(friendRows.getString("birthday")))
        );
        return user;
    }
}
