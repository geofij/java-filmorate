package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.LinkedHashSet;

@Component
@Deprecated
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {
    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public LinkedHashSet<Film> getCommonFilmsSortedByLikes(long firstId, long secondId) {
        return null;
    }
}
