package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void create(Film data);

    void update(Film data);

    List<Film> getAllData();

    Film getById(long id);

    boolean delete(long id);
}
