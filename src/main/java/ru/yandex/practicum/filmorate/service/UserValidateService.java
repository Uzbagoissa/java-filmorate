package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.HashMap;

@Service
public class UserValidateService {

    public void checkRemoveFriendValidate(UserStorage userStorage, Integer id, Integer friendId) {
        if (!userStorage.getUsers().get(id).getFriends().contains(friendId)) {
            throw new ValidationException("Этого пользователя уже нет в друзьях");
        }
    }

    public void checkAddFriendValidate(UserStorage userStorage, Integer id, Integer friendId) {
        if (userStorage.getUsers().get(id).getFriends().contains(friendId)) {
            throw new ValidationException("Этот пользователь уже есть в друзьях");
        }
    }

    public void checkUserValidate(Logger log, HashMap<Integer, User> users, Integer id) {
        if (!users.containsKey(id)){
            log.error("Такого пользователя не существует!, {}", id);
            throw new NotFoundException("Такого пользователя не существует!");
        }
    }

    public void checkCreateUserValidate(Logger log, HashMap<Integer, User> users, User user) {
        if (users.containsKey(user.getId())) {
            log.error("Такой пользователь уже существует!, {}", user);
            throw new ValidationException("Такой пользователь уже существует!");
        } else if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.error("Электронная почта не может быть пустой и должна содержать символ - @, {}", user);
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")) {
            log.error("Логин не может быть пустым и содержать пробелы!, {}", user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения не может быть в будущем!, {}", user);
            throw new ValidationException("Дата рождения не может быть в будущем!");
        }
    }
}
