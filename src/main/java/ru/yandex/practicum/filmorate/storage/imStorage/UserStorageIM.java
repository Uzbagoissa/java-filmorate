package ru.yandex.practicum.filmorate.storage.imStorage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.UserStorage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.imValidate.UserValidateIM;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class UserStorageIM implements UserStorage {
    private final UserValidateIM userValidateIM;
    private static final HashMap<Integer, User> users = new HashMap<>();
    private int userID = 1;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserStorageIM(UserValidateIM userValidateIM) {
        this.userValidateIM = userValidateIM;
    }

    @Override
    public HashMap<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User getUser(Integer id) {
        userValidateIM.checkUserValidate(log, users, id);
        log.info("Найден пользователь");
        return users.get(id);
    }

    @Override
    public ArrayList<User> getAllUsers() {
        log.info("Найдены пользователи");
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        userValidateIM.checkCreateUserValidate(log, users, user);
        if (user.getName() == null || user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(userID);
        users.put(userID, user);
        userID++;
        log.info("Добавлен новый пользователь, {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        userValidateIM.checkUserValidate(log, users, user.getId());
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен - , {}", user);
        return user;
    }
}
