package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllFriends(Integer id);
    List<User> getCommonFriends(Integer id, Integer otherId);
    User addFriend(Integer id, Integer friendId);
    User removeFriend(Integer id, Integer friendId);
}
