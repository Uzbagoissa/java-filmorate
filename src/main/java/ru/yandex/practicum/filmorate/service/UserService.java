package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final UserValidateService userValidateService = new UserValidateService();

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllFriends (Integer id) {
        userValidateService.checkUserValidateElse(userStorage, id);
        return userStorage.getUsers().values().stream()
                .filter(p -> userStorage.getUsers().get(id).getFriends().contains(p.getId()))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends (Integer id, Integer otherId) {
        userValidateService.checkUserValidateElse(userStorage, id);
        userValidateService.checkUserValidateElse(userStorage, otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < userStorage.getUsers().get(id).getFriends().size(); i++) {
            if (userStorage.getUsers().get(otherId).getFriends().contains(userStorage.getUsers().get(id).getFriends().get(i))){
                commonFriends.add(userStorage.getUsers().get(userStorage.getUsers().get(id).getFriends().get(i)));
            }
        }
        return commonFriends;
    }

    public User addFriend (Integer id, Integer friendId) {
        userValidateService.checkUserValidateElse(userStorage, id);
        userValidateService.checkUserValidateElse(userStorage, friendId);
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
        return userStorage.getUsers().get(id);
    }

    public User removeFriend (Integer id, Integer friendId) {
        userValidateService.checkUserValidateElse(userStorage, id);
        userValidateService.checkUserValidateElse(userStorage, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        return userStorage.getUsers().get(id);
    }
}
