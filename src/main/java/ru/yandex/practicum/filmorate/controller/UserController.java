package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;

@RestController
@RequestMapping("/users")
public class UserController {
    private final HashSet<User> users = new HashSet<>();

    @GetMapping()
    public HashSet<User> getUsers() {
        return users;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) throws UserAlreadyExistException, InvalidEmailException, InvalidLoginException, InvalidBirthdayException {
        if (users.contains(user)){
            throw new UserAlreadyExistException("Такой пользователь уже существует!");
        } else if (user.getEmail().trim().equals("") || !user.getEmail().contains("@")){
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")){
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())){
            throw new InvalidBirthdayException("Дата рождения не может быть в будущем!");
        } else if (user.getName().trim().equals("")){
            user.setName(user.getLogin());
            users.add(user);
            return user;
        } else {
            users.add(user);
            return user;
        }
    }

    @PutMapping()
    public User renewUser(@RequestBody User user) throws InvalidEmailException, InvalidLoginException, InvalidBirthdayException {
        if (user.getEmail().trim().equals("") || !user.getEmail().contains("@")){
            throw new InvalidEmailException("Электронная почта не может быть пустой и должна содержать символ - @");
        } else if (user.getLogin().equals("") || user.getLogin().contains(" ")){
            throw new InvalidLoginException("Логин не может быть пустым и содержать пробелы!");
        } else if (user.getBirthday().isAfter(LocalDate.now())){
            throw new InvalidBirthdayException("Дата рождения не может быть в будущем!");
        } else if(users.contains(user)) {
            users.remove(user);
            if (user.getName().trim().equals("")){
                user.setName(user.getLogin());
            }
            users.add(user);
            return user;
        } else {
            if (user.getName().trim().equals("")){
                user.setName(user.getLogin());
            }
            users.add(user);
            return user;
        }
    }
}
