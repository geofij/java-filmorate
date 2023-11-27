package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

@Repository
@RequiredArgsConstructor
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update("merge into likes (film_id, user_id) values (?, ?)", filmId, userId);
    }

    @Override
    public boolean deleteLike(long filmId, long userId) {
        return jdbcTemplate.update("delete from likes where film_id = ? and user_id = ?", filmId, userId) > 0;
    }
}
