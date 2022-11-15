package ru.yandex.practicum.filmorate.validate.dbValidate;

import org.slf4j.Logger;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Service
public class UserValidateDB{

    public void checkUserValidate(Logger log, SqlRowSet userRows, Integer id) {
        if(!userRows.next()) {
            log.error("Такого пользователя не существует! {}", id);
            throw new NotFoundException("Такого пользователя не существует!");
        }
    }

    public void checkAddFriendValidate(Logger log, SqlRowSet idCheckRows, Integer friendId, Integer id) {
        if (idCheckRows.next()){
            log.error("Пользователь, {} уже есть в друзьях пользователя {}", friendId, id);
            throw new ValidationException("Этот пользователь уже есть в друзьях");
        }
    }

    public void checkCreateUserValidate(Logger log, SqlRowSet userRows, User user) {
        if(userRows.next()) {
            log.error("Такой пользователь уже существует! {}", user);
            throw new ValidationException("Такой пользователь уже существует!");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ - @ {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы! {}", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем! {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }

}
