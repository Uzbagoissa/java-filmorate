package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public interface UserStorage {
    HashMap<Integer, User> getUsers();
    ArrayList<User> getAllUsers();
    User createUser(User user);
    User updateUser(User user);
}
