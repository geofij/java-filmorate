package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
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
    private final FeedStorage feedStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage, FriendsStorage friendsStorage, FeedStorage feedStorage) {
        this.userStorage = storage;
        this.friendsStorage = friendsStorage;
        this.feedStorage = feedStorage;
    }

    public void create(User user) {
        userStorage.create(user);
    }

    public void update(User user) {
        userStorage.getById(user.getId());
        userStorage.update(user);
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
        feedStorage.addFeed(idUser, Event.FRIEND, Operation.ADD, idFriend);
    }

    public boolean deleteFriend(long idUser, long idFriend) {
        userStorage.getById(idUser);
        userStorage.getById(idFriend);
        feedStorage.addFeed(idUser, Event.FRIEND, Operation.REMOVE, idFriend);

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
