package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public Director create(@Valid @RequestBody Director director) {
        Director newDirector = directorService.create(director);
        log.info("Creating director {}", director);
        return newDirector;
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        directorService.update(director);
        log.info("Updating director {}", director);
        return directorService.get(director.getId());
    }

    @GetMapping
    public List<Director> getDirectors() {
        log.info("Getting all directors");
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable("id") long id) {
        log.info("Getting director id-{}", id);
        return directorService.get(id);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") long id) {
        return directorService.delete(id);
    }
}
