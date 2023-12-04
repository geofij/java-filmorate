package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    Director create(Director data);

    Director update(Director data);

    List<Director> getAllDirector();

    Director getById(long id);

    boolean delete(long id);
}
