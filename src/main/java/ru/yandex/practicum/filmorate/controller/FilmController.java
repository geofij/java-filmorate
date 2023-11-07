package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotLikeFilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film, FilmService> {
    public static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public FilmController(FilmService service) {
       super(service);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return super.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return super.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Getting all films");
        return super.getAllData();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        checkContainData(id);
        log.info("Getting film id-{}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        checkContainData(id);
        log.info("Adding like to film id-{} by user id-{}", id, userId);
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        checkContainData(id);
        if (service.get(id).getLikes() == null || !service.get(id).getLikes().contains(userId)) {
            throw new UserNotLikeFilmException(String.format("Film have not like from user id-%s.", userId));
        }
        log.info("Deleting like from film id-{} by user id-{}", id, userId);
        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        return service.getPopular(Integer.parseInt(count));
    }

    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug("Fail validation film {}", film);
            throw new ValidationException("Film release date is invalid");
        }
    }
}
