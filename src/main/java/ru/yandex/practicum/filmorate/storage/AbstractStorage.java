package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

public interface AbstractStorage<T extends BaseUnit> {
    void create(T data);

    void update(T data);

    List<T> getAllData();

    T getById(long id);
}
