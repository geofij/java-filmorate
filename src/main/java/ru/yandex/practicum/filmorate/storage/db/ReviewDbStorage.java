package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewDbStorage implements ReviewStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(Review data) {
        jdbcTemplate.update("insert into review (film_id, user_id, is_positive, description) values (?, ?, ?, ?)",
                data.getFilmId(),
                data.getUserId(),
                data.getIsPositive(),
                data.getContent()
        );
    }

    @Override
    public void update(Review data) {
        jdbcTemplate.update("update review set is_positive = ?, description = ? where id = ?",
                data.getIsPositive(),
                data.getIsPositive(),
                data.getReviewId()
        );
    }

    @Override
    public Review getById(Long id) {
        List<Review> reviews = jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_like = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id where r.id = ? ",
                new ReviewRowMapper(),
                id
        );
        if (reviews.size() != 1) {
            throw new DataNotFoundException("User id-{} not found");
        }
        return reviews.get(0);
    }

    @Override
    public List<Review> getByFilmId(Long id) {
        return jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_like = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id where r.film_id = ? " +
                        "order by useful desc",
                new ReviewRowMapper(),
                id
        );
    }

    @Override
    public List<Review> getAll() {
        return jdbcTemplate.query(
                "select r.id , r.description , r.is_positive , r.user_id , r.film_id " +
                        ", ifnull(rl.useful, 0) as useful from review as r " +
                        "left join (select review_id, sum(case when is_like = true then 1 else -1 end) as useful " +
                        "from review_like group by review_id) as rl on rl.review_id = r.id " +
                        "order by useful desc",
                new ReviewRowMapper()
        );
    }

    @Override
    public void delete(Review data) {
        jdbcTemplate.update("delete from review where id = ?",
                data.getReviewId()
        );
    }

    @Override
    public void addReaction(Review review, Boolean isPositive, User user) {
        jdbcTemplate.update("merge into review_like (user_id, review_id, is_positive) values (?, ?, ?)",
                user.getId(),
                review.getReviewId(),
                isPositive
        );
    }

    @Override
    public void delReaction(Review review, User user) {
        jdbcTemplate.update("delete from review_like where user_id = ? and review_id = ?",
                user.getId(),
                review.getReviewId()
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
