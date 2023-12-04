package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void create(User data);

    User update(User data);

    List<User> getAllData();

    User getById(long id);

    boolean delete(long id);
}
