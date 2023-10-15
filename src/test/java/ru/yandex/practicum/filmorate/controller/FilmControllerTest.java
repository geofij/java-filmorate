package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController controller;

    @BeforeEach
    void init() {
        controller = new FilmController();
    }

    @Test
    void validateDateBeforeValid() {
        Film film = Film.builder()
                .id(2L)
                .name("Name")
                .description("Description")
                .duration(75)
                .releaseDate(LocalDate.of(1800, 1, 1))
                .build();

        assertThrows(ValidationException.class, () -> controller.validate(film), "Валидация прошла успешно.");
    }

    @Test
    void validateDateAfterInvalid() {
        Film film = Film.builder()
                .id(2L)
                .name("Name")
                .description("Description")
                .duration(75)
                .releaseDate(LocalDate.of(2000, 1, 1))
                .build();

        controller.validate(film);
    }
}