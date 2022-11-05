package ru.yandex.practicum.filmorate.storage.storageInterfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;

public interface UserStorage {
    User getUser(Integer id);
    HashMap<Integer, User> getUsers();
    List<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
}
