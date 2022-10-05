package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserValidateService;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserValidateService userValidateService = new UserValidateService();
    private static final HashMap<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int userID = 1;

    @GetMapping()
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        userValidateService.checkPOSTUserValidate(log, users, user);
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

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        userValidateService.checkPUTUserValidate(log, users, user);
        if (user.getName().trim().equals("")) {
            user.setName(user.getLogin());
        }
        user.setId(user.getId());
        users.put(user.getId(), user);
        log.info("Пользователь обновлен - , {}", user);
        return user;
    }
}
