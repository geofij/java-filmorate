package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.LinkedHashSet;
import java.util.List;

@Component
@Deprecated
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {
    @Override
    public boolean delete(long id) {
        return false;
    }

    @Override
    public List<Film> getFilteredData(Long genreId, Integer releaseYear) {
        return null;
    }

    @Override
    public List<Film> getSortedFilmsByDirector(String sortType, long directorId) {
        return null;
    }

    @Override
    public LinkedHashSet<Film> getCommonFilmsSortedByLikes(long firstId, long secondId) {
        return null;
    }

    public List<Film> findByTitle(String query) {
        return null;
    }

    @Override
    public List<Film> findByDirector(String query) {
        return null;
    }
}
