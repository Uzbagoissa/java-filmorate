package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.UserService;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("userStorageDB") UserStorage userStorage, @Qualifier("userServiceDB") UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }
    @GetMapping()
    public List<User> getAllUsers() {
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
