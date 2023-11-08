package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotLikeFilmException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService extends BaseService<Film> {
    @Autowired
    public FilmService(FilmStorage storage) {
        super(storage);
    }

    public Film addLike(long idFilm, long idUser) {
        Set<Long> likeList;
        Film film = storage.getById(idFilm);

        if (film.getLikes() == null) {
            likeList = new HashSet<>();
            likeList.add(idUser);

            film.setLikes(likeList);
        } else {
            film.getLikes().add(idUser);
        }

        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        Film film = storage.getById(idFilm);

        if (get(idFilm).getLikes() == null || !get(idFilm).getLikes().contains(idUser)) {
            throw new UserNotLikeFilmException(String.format("Film have not like from user id-%s.", idUser));
        }

        if (film.getLikes() != null) {
            film.getLikes().remove(idUser);
        }

        return film;
    }

    public List<Film> getPopular(int count) {
        List<Film> films = storage.getAllData();

        if (count > films.size()) {
            count = films.size();
        }

        return films.stream()
                .sorted(Comparator.comparing(film -> {
                    if (film.getLikes() == null) {
                        return 0;
                    }
                    return film.getLikes().size();
                }, Comparator.reverseOrder()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
