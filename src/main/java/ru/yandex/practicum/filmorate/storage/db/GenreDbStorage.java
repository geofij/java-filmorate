package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllData() {
        String sqlQuery = "select * from genres";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> GenreDbStorage.createGenre(rs, rowNum, "id"));
    }

    @Override
    public Genre getById(long id) {
        String sqlQuery = "select * from genres where id = ?";
        List<Genre> genreList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> GenreDbStorage.createGenre(rs, rowNum, "id"), id);

        if (genreList.size() != 1) {
            throw new DataNotFoundException("Genre id-{} not found");
        }

        return genreList.get(0);
    }

    public static Genre createGenre(ResultSet rs, int rowNum, String columnName) throws SQLException {
        return Genre.builder()
                .id(rs.getLong(columnName))
                .name(rs.getString("name"))
                .build();
    }
}
