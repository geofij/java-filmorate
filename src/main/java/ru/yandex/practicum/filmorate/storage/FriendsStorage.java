package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    boolean deleteFriend(long userId, long friendId);

    void addFriend(long userId, long friendId);

    List<User> getUserFriends(long userId);
}
