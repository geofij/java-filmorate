package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmControllerTest {
    private final FilmController controller;

    @Test
    void validateDateBeforeValid() {
        Film film = Film.builder()
                .id(2L)
                .name("Name")
                .description("Description")
                .duration(75)
                .releaseDate(LocalDate.of(1800, 1, 1))
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
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
                .mpa(Mpa.builder()
                        .id(1L)
                        .build())
                .build();

        controller.validate(film);
    }
}