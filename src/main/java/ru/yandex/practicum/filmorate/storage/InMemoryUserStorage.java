package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidateService;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InMemoryUserStorage implements UserStorage{
    private UserValidateService userValidateService = new UserValidateService();
    private static final HashMap<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int userID = 1;

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User getUser(Integer id) {
        userValidateService.checkGetUserValidate(log, users, id);
        return users.get(id);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        userValidateService.checkCreateUserValidate(log, users, user);
        if (user.getName() == null) {
            user.setName(user.getLogin());
            user.setId(userID);
            users.put(userID, user);
            userID++;
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        } else {
            user.setId(userID);
            users.put(userID, user);
            userID++;
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        }
    }

    @Override
    public User updateUser(User user) {
        userValidateService.checkUpdateUserValidate(log, users, user);
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен - , {}", user);
        return user;
    }

}
