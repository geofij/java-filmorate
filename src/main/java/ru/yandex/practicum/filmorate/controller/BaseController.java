package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.service.BaseService;

import java.util.List;

@Slf4j
public abstract class BaseController<T extends BaseUnit, K extends BaseService<T>> {
    protected final K service;
    private long generateId;

    protected BaseController(K service) {
        this.service = service;
    }

    public T create(T data) {
        validate(data);
        data.setId(++generateId);
        service.create(data);
        log.info("Creating data {}", data);
        return data;
    }

    public T update(T data) {
        validate(data);
        service.update(data);
        log.info("Updating data {}", data);
        return data;
    }

    public List<T> getAllData() {
        return service.getAllData();
    }

    public abstract void validate(T data);
}
