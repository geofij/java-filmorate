package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public static Genre createGenre(ResultSet rs, String columnName) throws SQLException {
        return Genre.builder()
                .id(rs.getLong(columnName))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Genre> getAllData() {
        return jdbcTemplate.query("select * from genres",
                (rs, rowNum) -> GenreDbStorage.createGenre(rs, "id"));
    }

    @Override
    public Genre getById(long id) {
        List<Genre> genreList = jdbcTemplate.query("select * from genres where id = ?",
                (rs, rowNum) -> GenreDbStorage.createGenre(rs, "id"), id);

        if (genreList.size() != 1) {
            throw new DataNotFoundException("Genre id-{} not found");
        }

        return genreList.get(0);
    }
}
