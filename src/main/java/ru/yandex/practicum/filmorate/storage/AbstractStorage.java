package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

@Deprecated
public interface AbstractStorage<T extends BaseUnit> {
    void create(T data);

    T update(T data);

    List<T> getAllData();

    T getById(long id);
}
