package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.FeedDbStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;

    private final FilmStorage filmStorage;
    private final FeedStorage feedStorage;

    @Autowired
    public ReviewService(@Qualifier("reviewDbStorage") ReviewStorage reviewStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage,
                         FeedStorage feedStorage) {
        this.reviewStorage = reviewStorage;
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.feedStorage = feedStorage;
    }

    public Review create(Review data) {
        userStorage.getById(data.getUserId());
        filmStorage.getById(data.getFilmId());
        Review review = reviewStorage.create(data);
        feedStorage.addFeed(review.getUserId(), Event.REVIEW, Operation.ADD, review.getReviewId());
        return review;
    }

    public Review update(Review data) {
        reviewStorage.getById(data.getReviewId());
        Review review = reviewStorage.update(data);
        feedStorage.addFeed(review.getUserId(), Event.REVIEW, Operation.UPDATE, review.getReviewId());
        return review;
    }

    public Boolean delete(Long id) {
        Review data = reviewStorage.getById(id);
        reviewStorage.delete(id);
        feedStorage.addFeed(data.getUserId(), Event.REVIEW, Operation.REMOVE, data.getReviewId());
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
        feedStorage.addFeed(userId, Event.LIKE, Operation.ADD, reviewId);
        reviewStorage.addReaction(reviewId, isPositive, userId);
    }

    public void delReaction(Long reviewId, Long userId) {
        reviewStorage.getById(reviewId);
        userStorage.getById(userId);
        feedStorage.addFeed(userId, Event.LIKE, Operation.REMOVE, reviewId);
        reviewStorage.delReaction(reviewId, userId);
    }
}
