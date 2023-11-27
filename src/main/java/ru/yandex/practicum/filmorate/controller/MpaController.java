package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {
    private final MpaService service;

    @GetMapping
    public List<Mpa> getALl() {
        log.info("Getting all MPA");
        return service.getALlMpa();
    }

    @GetMapping("/{id}")
    public Mpa getMpaById(@PathVariable("id") long id) {
        log.info("Getting MPA id-{}", id);
        return service.get(id);
    }
}
