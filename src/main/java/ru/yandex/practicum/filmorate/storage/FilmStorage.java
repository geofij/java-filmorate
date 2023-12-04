package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.LinkedHashSet;
import java.util.List;

public interface FilmStorage {
    void create(Film data);

    Film update(Film data);

    List<Film> getAllData();

    Film getById(long id);

    boolean delete(long id);

    List<Film> getSortedFilmsByDirector(String sortType, long directorId);

    LinkedHashSet<Film> getCommonFilmsSortedByLikes(long userId, long friendId);
}
