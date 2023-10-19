package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {
    public static final LocalDate START_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film {}", film);
        return super.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Updating film {}", film);
        return super.update(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Getting all films");
        return super.getAllData();
    }

    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(START_RELEASE_DATE)) {
            log.debug("Fail validation film {}", film);
            throw new ValidationException("Film release date is invalid");
        }
    }
}
