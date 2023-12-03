package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage, FriendsStorage friendsStorage) {
        this.userStorage = storage;
        this.friendsStorage = friendsStorage;
    }

    public void create(User user) {
        userStorage.create(user);
    }

    public User update(User user) {
        userStorage.getById(user.getId());
        return userStorage.update(user);
    }

    public boolean delete(long id) {
        return userStorage.delete(id);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllData();
    }

    public User get(long id) {
        return userStorage.getById(id);
    }

    public void addFriend(long idUser, long idFriend) {
        userStorage.getById(idUser);
        userStorage.getById(idFriend);

        friendsStorage.addFriend(idUser, idFriend);
    }

    public boolean deleteFriend(long idUser, long idFriend) {
        userStorage.getById(idUser);
        userStorage.getById(idFriend);

        return friendsStorage.deleteFriend(idUser, idFriend);
    }

    public List<User> getFriends(long id) {
        userStorage.getById(id);
        return friendsStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(long idFirstUser, long idSecondUser) {
        userStorage.getById(idFirstUser);
        userStorage.getById(idSecondUser);

        Set<User> friendsFirst = new HashSet<>(friendsStorage.getUserFriends(idFirstUser));
        Set<User> friendsSecond = new HashSet<>(friendsStorage.getUserFriends(idSecondUser));

        if (friendsFirst.isEmpty() || friendsSecond.isEmpty()) {
            return new ArrayList<>();
        }

        friendsFirst.retainAll(friendsSecond);

        return new ArrayList<>(friendsFirst);
    }
}
