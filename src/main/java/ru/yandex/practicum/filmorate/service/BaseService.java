package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.List;

public class BaseService<T extends BaseUnit> {
    protected final AbstractStorage<T> storage;

    public BaseService(AbstractStorage<T> storage) {
        this.storage = storage;
    }

    public T get(long id) {
        return storage.getById(id);
    }

    public void create(T data) {
        storage.create(data);
    }

    public void update(T data) {
        storage.update(data);
    }

    public List<T> getAllData() {
        return storage.getAllData();
    }
}
