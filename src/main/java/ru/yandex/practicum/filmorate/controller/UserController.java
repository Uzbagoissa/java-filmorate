package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserValidateService;//
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }
    @GetMapping()
    public ArrayList<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return userStorage.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable("id") Integer id) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id,
                                        @PathVariable("otherId") Integer otherId) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        if (otherId <= 0) {
            throw new NotFoundException("otherId");
        }
        return userService.getCommonFriends(id, otherId);
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping()
    public User updateUser(@RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id,
                           @PathVariable("friendId") Integer friendId) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        if (friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable("id") Integer id,
                              @PathVariable("friendId") Integer friendId) {
        if (id <= 0) {
            throw new NotFoundException("id");
        }
        if (friendId <= 0) {
            throw new NotFoundException("friendId");
        }
        return userService.removeFriend(id, friendId);
    }

}
