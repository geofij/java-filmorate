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
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validation.FilmSearchByValidator.SEARCH_BY_DEFAULT;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    public static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final FilmService service;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validate(film);
        log.info("Creating film {}", film);
        Long filmId = service.create(film).getId();
        return service.get(filmId);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validate(film);
        log.info("Updating film {}", film);
        return service.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
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
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count,
                                      @RequestParam(required = false) Long genreId,
                                      @RequestParam(required = false) Integer year) {
        log.info("Getting {} popular films. Filtering is {}", count, getFilteringLogRecord(genreId, year));
        return service.getPopular(count, genreId, year);
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

    @GetMapping("/common")
    public LinkedHashSet<Film> getCommonFilmsSortedByLikes(@RequestParam("userId") long userId,
                                                           @RequestParam("friendId") long friendId) {
        log.info("Getting common films for user:{}, and user:{}", userId, friendId);
        return service.getCommonFilmsSortedByLikes(userId, friendId);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug("Fail validation film {}", film);
            throw new ValidationException("Film release date is invalid");
        }
    }

    private String getFilteringLogRecord(Long genreId, Integer releaseYear) {
        StringBuilder filterLoggerBuilder = new StringBuilder();
        if (genreId == null && releaseYear == null) {
            filterLoggerBuilder.append("disabled");
        } else {
            filterLoggerBuilder.append("enabled with filtering params: ");
            Map<String, String> filteringParams = new HashMap<>();
            if (genreId != null)
                filteringParams.put("genreId", String.valueOf(genreId));
            if (releaseYear != null)
                filteringParams.put("releaseYear", String.valueOf(releaseYear));
            filterLoggerBuilder.append(filteringParams);
        }
        return filterLoggerBuilder.toString();
    }
}
