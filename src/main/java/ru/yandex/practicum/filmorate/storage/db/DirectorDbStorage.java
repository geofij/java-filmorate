package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Director create(Director data) {
        String sqlQuery = "insert into director (name) values (?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(x -> {
                    PreparedStatement stmt = x.prepareStatement(sqlQuery, new String[]{"id"});
                    stmt.setString(1, data.getName());
                    return stmt;
                }, keyHolder
        );

        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Director data) {
        jdbcTemplate.update("update director set name = ? where id = ?",
                data.getName(),
                data.getId());
    }

    @Override
    public List<Director> getAllDirector() {
        return jdbcTemplate.query("select * from director", DirectorDbStorage::createDirector);
    }

    @Override
    public Director getById(long id) {
        List<Director> directorList = jdbcTemplate.query("select * from director where id = ?",
                DirectorDbStorage::createDirector, id);

        if (directorList.size() != 1) {
            throw new DataNotFoundException("Director id-{} not found");
        }

        return directorList.get(0);
    }

    @Override
    public boolean delete(long id) {
        getById(id);
        jdbcTemplate.update("delete from director where id = ?", id);
        return true;
    }

    public static Director createDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}
