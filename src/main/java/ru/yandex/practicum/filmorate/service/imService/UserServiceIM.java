package ru.yandex.practicum.filmorate.service.imService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.service.serviceInterfaces.UserService;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.storageInterfaces.UserStorage;
import ru.yandex.practicum.filmorate.validate.imValidate.UserValidateIM;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceIM implements UserService {
    private final UserStorage userStorage;
    private final UserValidateIM userValidateIM;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserServiceIM(@Qualifier("userStorageIM") UserStorage userStorage,
                         UserValidateIM userValidateIM) {
        this.userStorage = userStorage;
        this.userValidateIM = userValidateIM;
    }

    @Override
    public List<User> getAllFriends(Integer id) {
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        return userStorage.getUsers().values().stream()
                .filter(p -> userStorage.getUsers().get(id).getFriends().contains(p.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), otherId);
        ArrayList<User> commonFriends = new ArrayList<>();
        for (int i = 0; i < userStorage.getUsers().get(id).getFriends().size(); i++) {
            if (userStorage.getUsers().get(otherId).getFriends().contains(userStorage.getUsers().get(id).getFriends().get(i))) {
                commonFriends.add(userStorage.getUsers().get(userStorage.getUsers().get(id).getFriends().get(i)));
            }
        }
        return commonFriends;
    }

    @Override
    public User addFriend(Integer id, Integer friendId) {
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), friendId);
        userValidateIM.checkAddFriendValidate(log, userStorage, id, friendId);
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
        return userStorage.getUsers().get(id);
    }

    @Override
    public User removeFriend(Integer id, Integer friendId) {
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), id);
        userValidateIM.checkUserValidate(log, userStorage.getUsers(), friendId);
        userValidateIM.checkRemoveFriendValidate(log, userStorage, id, friendId);
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        return userStorage.getUsers().get(id);
    }
}
