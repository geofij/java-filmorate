package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public List<SimpleEntry<Long, Long>> getAllData() {
        return jdbcTemplate.query(("select * from likes"),
                (rs, rownum) -> new SimpleEntry<>(rs.getLong("user_id"),
                        rs.getLong("film_id")));
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update("merge into likes (film_id, user_id) values (?, ?)", filmId, userId);
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {
        return jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", filmId, userId) > 0;
    }
}
