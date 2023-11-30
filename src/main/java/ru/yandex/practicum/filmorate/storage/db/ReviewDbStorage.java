package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Review create(Review data) {
        String sqlQuery = "insert into review (film_id, user_id, is_positive, description) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(x -> {
                    PreparedStatement stmt = x.prepareStatement(sqlQuery, new String[]{"id"});
                    stmt.setLong(1, data.getFilmId());
                    stmt.setLong(2, data.getUserId());
                    stmt.setBoolean(3, data.getIsPositive());
                    stmt.setString(4, data.getContent());
                    return stmt;
                }, keyHolder
        );
        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Review update(Review data) {
        jdbcTemplate.update("update review set is_positive = ?, description = ? where id = ?",
                data.getIsPositive(),
                data.getContent(),
                data.getReviewId()
        );
        return getById(data.getReviewId());
    }

    @Override
    public Review getById(Long id) {
        List<Review> reviews = jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_positive = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id where r.id = ? ",
                new ReviewRowMapper(),
                id
        );
        if (reviews.size() != 1) {
            throw new DataNotFoundException("Review id-{} not found");
        }
        return reviews.get(0);
    }

    @Override
    public List<Review> getByFilmId(Long id, Integer count) {
        return jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_positive = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id where r.film_id = ? " +
                        "order by useful desc limit ?",
                new ReviewRowMapper(),
                id,
                count
        );
    }

    @Override
    public List<Review> getAll(Integer count) {
        return jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_positive = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id " +
                        "order by useful desc limit ?",
                new ReviewRowMapper(),
                count
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("delete from review where id = ?",
                id
        );
    }

    @Override
    public void addReaction(Long reviewId, Boolean isPositive, Long userId) {
        jdbcTemplate.update("merge into review_like (user_id, review_id, is_positive) values (?, ?, ?)",
                userId,
                reviewId,
                isPositive
        );
    }

    @Override
    public void delReaction(Long reviewId, Long userId) {
        jdbcTemplate.update("delete from review_like where user_id = ? and review_id = ?",
                userId,
                reviewId
        );
    }

    private static class ReviewRowMapper implements RowMapper<Review> {

        @Override
        public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Review.builder()
                    .reviewId(rs.getLong("id"))
                    .content(rs.getString("description"))
                    .isPositive(rs.getBoolean("is_positive"))
                    .userId(rs.getLong("user_id"))
                    .filmId(rs.getLong("film_id"))
                    .useful(rs.getLong("useful"))
                    .build();
        }
    }


}
