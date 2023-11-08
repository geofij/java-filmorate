package ru.yandex.practicum.filmorate.service;

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

        if (user.getFriendIds() == null) {
            friendList = new HashSet<>();
            friendList.add(idFriend);

            user.setFriendIds(friendList);
        } else {
            user.getFriendIds().add(idFriend);
        }

        if (friend.getFriendIds() == null) {
            friendList = new HashSet<>();
            friendList.add(idUser);

            friend.setFriendIds(friendList);
        } else {
            friend.getFriendIds().add(idUser);
        }

        return user;
    }

    public User deleteFriend(long idUser, long idFriend) {
        User user = storage.getById(idUser);
        User friend = storage.getById(idFriend);

        if (user.getFriendIds() != null) {
            user.getFriendIds().remove(idFriend);
        }

        if (friend.getFriendIds() != null) {
            friend.getFriendIds().remove(idUser);
        }

        return user;
    }

    public List<User> getFriends(long id) {
        return storage.getById(id).getFriendIds().stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long idFirstUser, long idSecondUser) {
        User firstUser = storage.getById(idFirstUser);
        User secondUser = storage.getById(idSecondUser);

        if (firstUser.getFriendIds() == null || secondUser.getFriendIds() == null) {
            return new ArrayList<>();
        }

        Set<Long> friendsFirst = new HashSet<>(firstUser.getFriendIds());
        friendsFirst.retainAll(secondUser.getFriendIds());

        return friendsFirst.stream()
                .map(storage::getById)
                .collect(Collectors.toList());
    }
}
