package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidateService;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class InMemoryUserStorage implements UserStorage {
    private final UserValidateService userValidateService;
    private static final HashMap<Integer, User> users = new HashMap<>();
    private int userID = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public InMemoryUserStorage(UserValidateService userValidateService) {
        this.userValidateService = userValidateService;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User getUser(Integer id) {
        userValidateService.checkUserValidate(log, users, id);
        return users.get(id);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        userValidateService.checkCreateUserValidate(log, users, user);
        if (user.getName().trim().equals("")) {
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
        userValidateService.checkUserValidate(log, users, user.getId());
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен - , {}", user);
        return user;
    }

}
