package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        Film newFilm = Film.builder()
                .id(1L)
                .name("Film")
                .description("Film")
                .releaseDate(LocalDate.of(2000, 11, 11))
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(new HashSet<>())
                .directors(new HashSet<>())
                .duration(200)
                .build();
        FilmDbStorage filStorage = new FilmDbStorage(jdbcTemplate);
        filStorage.create(newFilm);

        Film savedFilm = filStorage.getById(1);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

}