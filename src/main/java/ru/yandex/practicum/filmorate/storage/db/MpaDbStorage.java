package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    private static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    @Override
    public List<Mpa> getAllData() {
        return jdbcTemplate.query("select * from mpa",
                MpaDbStorage::createMpa);
    }

    @Override
    public Mpa getById(long id) {
        List<Mpa> mpaList = jdbcTemplate.query("select * from mpa where id = ?",
                MpaDbStorage::createMpa, id);

        if (mpaList.size() != 1) {
            throw new DataNotFoundException("MPA id-{} not found");
        }

        return mpaList.get(0);
    }
}
