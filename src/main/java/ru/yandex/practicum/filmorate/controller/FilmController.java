package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.SearchByConstraint;

import java.time.LocalDate;
import java.util.List;

import static ru.yandex.practicum.filmorate.validation.FilmSearchByValidator.SEARCH_BY_DEFAULT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final FilmService service;
    public static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private long idCounter;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        ++idCounter;
        film.setId(idCounter);
        log.info("Creating film {}", film);
        service.create(film);
        return service.get(film.getId());
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        log.info("Updating film {}", film);
        return service.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Getting all films");
        return service.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") long id) {
        log.info("Getting film id-{}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Adding like to film id-{} by user id-{}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean deleteLike(@PathVariable("id") long id, @PathVariable("userId") long userId) {
        log.info("Deleting like from film id-{} by user id-{}", id, userId);
        return service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") String count) {
        log.info("Getting {} popular films", count);
        return service.getPopular(Integer.parseInt(count));
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") long id) {
        return service.delete(id);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam(value = "query", defaultValue = "") String queryExpression,
                                  @RequestParam(value = "by", required = false, defaultValue = SEARCH_BY_DEFAULT)
                                  @SearchByConstraint String searchByLine) {
        return service.findFilmByTitleDirector(queryExpression, searchByLine);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getSortedFilmsByDirector(@RequestParam("sortBy") String sortType, @PathVariable("directorId") long directorId) {
        log.info("Getting director id-{}'s films sorted by {}", directorId, sortType);
        return service.getSortedFilmsByDirector(sortType, directorId);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug("Fail validation film {}", film);
            throw new ValidationException("Film release date is invalid");
        }
    }
}
