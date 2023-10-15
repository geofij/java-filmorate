package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class BaseController<T extends BaseUnit> {
    private final Map<Long, T> storage = new HashMap<>();
    private long generateId;

    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        storage.put(data.getId(), data);
        return data;
    }

    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            log.debug("Fail update data {}", data);
            throw new DataNotFoundException(String.format("Data %s not found", data));
        }
        validate(data);
        storage.put(data.getId(), data);
        return data;
    }

    public List<T> getAllData() {
        return new ArrayList<>(storage.values());
    }

    public abstract void validate(T data);
}
