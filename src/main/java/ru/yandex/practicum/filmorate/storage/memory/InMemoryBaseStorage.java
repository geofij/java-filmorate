package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Deprecated
public class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {
    private final Map<Long, T> storage = new HashMap<>();

    @Override
    public T create(T data) {
        storage.put(data.getId(), data);
        return getById(data.getId());
    }

    @Override
    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            log.debug("Fail update data {}", data);
            throw new DataNotFoundException(String.format("Data %s not found", data));
        }
        storage.put(data.getId(), data);

        return getById(data.getId());
    }

    @Override
    public List<T> getAllData() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public T getById(long id) {
        if (!storage.containsKey(id)) {
            log.debug("Fail getting data id-{}", id);
            throw new DataNotFoundException(String.format("Data id-%s not found", id));
        }
        return storage.get(id);
    }
}
