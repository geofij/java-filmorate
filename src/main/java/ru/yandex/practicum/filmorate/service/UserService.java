package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User> {

    @Autowired
    public UserService(UserStorage storage) {
        super(storage);
    }

    public User addFriend(long idUser, long idFriend) {
        Set<Long> friendList;
        User user = storage.getById(idUser);
        User friend = storage.getById(idFriend);

        if (user.getFriends() == null) {
            friendList = new HashSet<>();
            friendList.add(idFriend);

            user.setFriends(friendList);
        } else {
            user.getFriends().add(idFriend);
        }

        if (friend.getFriends() == null) {
            friendList = new HashSet<>();
            friendList.add(idUser);

            friend.setFriends(friendList);
        } else {
            friend.getFriends().add(idUser);
        }

        return user;
    }

    public User deleteFriend(long idUser, long idFriend) {
        User user = storage.getById(idUser);
        User friend = storage.getById(idFriend);

        if (user.getFriends() != null) {
            user.getFriends().remove(idFriend);
        }

        if (friend.getFriends() != null) {
            friend.getFriends().remove(idUser);
        }

        return user;
    }

    public List<User> getFriends(long id) {
        return storage.getById(id).getFriends().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idFirstUser, long idSecondUser) {
        User firstUser = storage.getById(idFirstUser);
        User secondUser = storage.getById(idSecondUser);

        if (firstUser.getFriends() == null || secondUser.getFriends() == null) {
            return new ArrayList<>();
        }

        Set<Long> friendsFirst = new HashSet<>(firstUser.getFriends());
        friendsFirst.retainAll(secondUser.getFriends());

        return friendsFirst.stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }
}
