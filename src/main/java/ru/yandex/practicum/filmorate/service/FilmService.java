package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;
    private final DirectorStorage directorStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikesStorage likesStorage,
                       DirectorStorage directorStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
        this.directorStorage = directorStorage;
    }

    public void create(Film film) {
        filmStorage.create(film);
    }

    public Film update(Film film) {
        filmStorage.getById(film.getId());
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllData();
    }

    public Film get(long id) {
        return filmStorage.getById(id);
    }

    public void addLike(long idFilm, long idUser) {
        filmStorage.getById(idFilm);
        userStorage.getById(idUser);

        likesStorage.addLike(idFilm, idUser);
    }

    public boolean deleteLike(long idFilm, long idUser) {
        filmStorage.getById(idFilm);
        userStorage.getById(idUser);

        return likesStorage.deleteLike(idFilm, idUser);
    }

    public boolean delete(long id) {
        return filmStorage.delete(id);
    }

    public List<Film> getPopular(int count) {
        List<Film> films = filmStorage.getAllData();

        if (count > films.size()) {
            count = films.size();
        }

        return films.stream()
                .sorted(Comparator.comparing(Film::getRate, Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getSortedFilmsByDirector(String sortType, long directorId) {
        directorStorage.getById(directorId);
        return filmStorage.getSortedFilmsByDirector(sortType, directorId);
    }

    public LinkedHashSet<Film> getCommonFilmsSortedByLikes(long userId, long friendId) {
        userStorage.getById(userId);
        User secondUser = userStorage.getById(friendId);
        return filmStorage.getCommonFilmsSortedByLikes(userId, friendId);
    }
}
