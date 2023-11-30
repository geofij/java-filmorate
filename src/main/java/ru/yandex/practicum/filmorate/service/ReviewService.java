package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(@Qualifier("ReviewDbStorage") ReviewStorage reviewStorage) {
        this.reviewStorage = reviewStorage;
    }

    public void create (Review data) {
        reviewStorage.create(data);
    }

    public void update(Review data) {
        reviewStorage.getById(data.getReviewId());
        reviewStorage.update(data);
    }

    public void delete(Review data) {
        reviewStorage.getById(data.getReviewId());
        reviewStorage.delete(data);
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
}
