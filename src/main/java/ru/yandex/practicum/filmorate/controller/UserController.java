package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;

public class UserController {
    private final HashSet<User> users = new HashSet<>();

    @GetMapping()
    public HashSet<User> getUsers() {
        return users;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) throws UserAlreadyExistException, InvalidEmailException {
        if (users.contains(user)){
            throw new UserAlreadyExistException("Такой пользователь уже существует!");
        } else if (user.getEmail().equals("")){
            throw new InvalidEmailException("Отстутствует email пользователя!");
        } else {
            users.add(user);
            return user;
        }
    }

    @PutMapping()
    public User renewUser(@RequestBody User user) throws InvalidEmailException {
        if (user.getEmail().equals("")){
            throw new InvalidEmailException("Отстутствует email пользователя!");
        } else if(users.contains(user)) {
            users.add(user);
            return user;
        } else {
            users.add(user);
            return user;
        }
    }
}
