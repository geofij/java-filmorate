package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;

    private final FilmStorage filmStorage;

    @Autowired
    public ReviewService(@Qualifier("reviewDbStorage") ReviewStorage reviewStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.reviewStorage = reviewStorage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    public Review create(Review data) {
        userStorage.getById(data.getUserId());
        filmStorage.getById(data.getFilmId());
        return reviewStorage.create(data);
    }

    public Review update(Review data) {
        reviewStorage.getById(data.getReviewId());
        return reviewStorage.update(data);
    }

    public Boolean delete(Long id) {
        reviewStorage.getById(id);
        reviewStorage.delete(id);
        return true;
    }

    public Review getById(Long id) {
        return reviewStorage.getById(id);
    }

    public List<Review> getByFilmId(Long id, Integer count) {
        filmStorage.getById(id);
        return reviewStorage.getByFilmId(id, count);
    }

    public List<Review> getAll(Integer count) {
        return reviewStorage.getAll(count);
    }

    public void addReaction(Long reviewId, Boolean isPositive, Long userId) {
        reviewStorage.getById(reviewId);
        userStorage.getById(userId);
        reviewStorage.addReaction(reviewId, isPositive, userId);
    }

    public void delReaction(Long reviewId, Long userId) {
        reviewStorage.getById(reviewId);
        userStorage.getById(userId);
        reviewStorage.delReaction(reviewId, userId);
    }
}
