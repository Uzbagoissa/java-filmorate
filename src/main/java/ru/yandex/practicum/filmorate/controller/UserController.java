package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashSet<User> users = new HashSet<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private int userID = 1;

    @GetMapping()
    public HashSet<User> getUsers() {
        return users;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) throws UserAlreadyExistException, InvalidEmailException, InvalidLoginException, InvalidBirthdayException {
        if (users.contains(user)){
            log.error("Такой пользователь уже существует!, {}", user);
            throw new UserAlreadyExistException("Такой пользователь уже существует!");
        } else if (user.getEmail().trim().equals("") || !user.getEmail().contains("@")){
            log.error("Электронная почта не может быть пустой и должна содержать символ - @, {}", user);
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")){
            log.error("Логин не может быть пустым и содержать пробелы!, {}", user);
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())){
            log.error("Дата рождения не может быть в будущем!, {}", user);
            throw new InvalidBirthdayException("Дата рождения не может быть в будущем!");
        } else if (user.getName().trim().equals("")){
            user.setName(user.getLogin());
            user.setId(userID);
            users.add(user);
            userID = userID + 1;
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        } else {
            user.setId(userID);
            users.add(user);
            userID = userID + 1;
            log.info("Добавлен новый пользователь, {}", user);
            return user;
        }
    }

    @PutMapping()
    public User renewUser(@RequestBody User user) throws InvalidUserException {
        if (!users.contains(user)){
            log.error("Такого пользователя не существует!, {}", user);
            throw new InvalidUserException("Такого пользователя не существует!");
        } else {
            users.remove(user);
            if (user.getName().trim().equals("")){
                user.setName(user.getLogin());
            }
            user.setId(user.getId());
            users.add(user);
            log.info("Пользователь обновлен - , {}", user);
            return user;
        }
    }
}
