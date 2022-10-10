package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserValidateService userValidateService = new UserValidateService();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllFriends(Integer id) {
        userValidateService.checkUserValidate(log, userStorage.getUsers(), id);
        return userStorage.getUsers().values().stream()
                .filter(p -> userStorage.getUsers().get(id).getFriends().contains(p.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userValidateService.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateService.checkUserValidate(log, userStorage.getUsers(), otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < userStorage.getUsers().get(id).getFriends().size(); i++) {
            if (userStorage.getUsers().get(otherId).getFriends().contains(userStorage.getUsers().get(id).getFriends().get(i))) {
                commonFriends.add(userStorage.getUsers().get(userStorage.getUsers().get(id).getFriends().get(i)));
            }
        }
        return commonFriends;
    }

    public User addFriend(Integer id, Integer friendId) {
        userValidateService.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateService.checkUserValidate(log, userStorage.getUsers(), friendId);
        userValidateService.checkAddFriendValidate(userStorage, id, friendId);
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
        return userStorage.getUsers().get(id);
    }

    public User removeFriend(Integer id, Integer friendId) {
        userValidateService.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateService.checkUserValidate(log, userStorage.getUsers(), friendId);
        userValidateService.checkRemoveFriendValidate(userStorage, id, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        return userStorage.getUsers().get(id);
    }
}
