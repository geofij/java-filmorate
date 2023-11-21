package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllData() {
        String sqlQuery = "select * from mpa";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> MpaDbStorage.createMpa(rs, rowNum, "id"));
    }

    @Override
    public Mpa getById(long id) {
        String sqlQuery = "select * from mpa where id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> MpaDbStorage.createMpa(rs, rowNum, "id"), id);

        if (mpaList.size() != 1) {
            throw new DataNotFoundException("MPA id-{} not found");
        }

        return mpaList.get(0);
    }

    public static Mpa createMpa(ResultSet rs, int rowNum, String columnName) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong(columnName))
                .name(rs.getString("name"))
                .build();
    }
}
