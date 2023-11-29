package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
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
        service.update(film);
        log.info("Updating film {}", film);
        return service.get(film.getId());
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

    @DeleteMapping("/id")
    public boolean delete(@PathVariable("id") long id) {
        return service.delete(id);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug("Fail validation film {}", film);
            throw new ValidationException("Film release date is invalid");
        }
    }
}
