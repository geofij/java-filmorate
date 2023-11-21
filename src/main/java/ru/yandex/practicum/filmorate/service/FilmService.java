package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likesStorage = likesStorage;
    }

    public void create(Film film) {
        filmStorage.create(film);
    }

    public void update(Film film) {
        filmStorage.getById(film.getId());
        filmStorage.update(film);
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
}
