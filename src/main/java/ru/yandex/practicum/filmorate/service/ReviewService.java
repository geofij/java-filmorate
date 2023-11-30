package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;

    @Autowired
    public ReviewService(@Qualifier("reviewDbStorage") ReviewStorage reviewStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        this.reviewStorage = reviewStorage;
        this.userStorage = userStorage;
    }

    public Review create (Review data) {
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

    public List<Review> getByFilmId(Long id) {
        return reviewStorage.getByFilmId(id);
    }

    public List<Review> getAll() {
        return reviewStorage.getAll();
    }

    public void addReaction(Review review, Boolean isPositive, User user) {
        reviewStorage.getById(review.getReviewId());
        userStorage.getById(user.getId());
        reviewStorage.addReaction(review, isPositive, user);
    }

    public void delReaction(Review review, User user) {
        reviewStorage.getById(review.getReviewId());
        userStorage.getById(user.getId());
        reviewStorage.delReaction(review, user);
    }
}
