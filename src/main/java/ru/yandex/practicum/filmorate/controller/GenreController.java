package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService service;

    @GetMapping
    public List<Genre> getALl() {
        log.info("Getting all genres");
        return service.getALlGenre();
    }

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") long id) {
        log.info("Getting genre id-{}", id);
        return service.get(id);
    }
}
